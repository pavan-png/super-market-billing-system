// Updated script.js (only modified selectCash function; replace the whole file if needed)
let currentItems = [];
let grandTotal = 0;
let orderId = '';
let amountReceived = 0;
let changeAmount = 0;

function generateOrderId() {
    return crypto.randomUUID();
}

window.onload = function() {
    orderId = generateOrderId();
    document.getElementById('orderIdDisplay').textContent = orderId;
};

function addItem() {
    const productId = document.getElementById('productId').value;
    const quantity = parseInt(document.getElementById('quantity').value);

    if (!productId || !quantity || quantity <= 0) {
        alert('Please enter valid Product ID and Quantity');
        return;
    }

    fetch(`/api/products/${productId}`)
        .then(response => {
            if (!response.ok) throw new Error('Product not found');
            return response.json();
        })
        .then(product => {
            const subtotal = product.price * quantity;
            const item = {
                product: product,
                quantity: quantity,
                subtotal: subtotal
            };
            currentItems.push(item);
            grandTotal += subtotal;

            updateTable();
            document.getElementById('productId').value = '';
            document.getElementById('quantity').value = '';
        })
        .catch(error => {
            alert('Error: ' + error.message);
        });
}

function updateTable() {
    const tbody = document.querySelector('#billingTable tbody');
    tbody.innerHTML = '';

    currentItems.forEach((item, index) => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${item.product.id}</td>
            <td>${item.product.name}</td>
            <td>${item.quantity}</td>
            <td>${item.product.price.toFixed(2)}</td>
            <td>${item.subtotal.toFixed(2)}</td>
            <td><button onclick="removeItem(${index})" class="btn btn-danger btn-sm">Remove</button></td>
        `;
        tbody.appendChild(row);
    });

    document.getElementById('grandTotal').textContent = `Grand Total: ${grandTotal.toFixed(2)}`;
}

function removeItem(index) {
    grandTotal -= currentItems[index].subtotal;
    currentItems.splice(index, 1);
    updateTable();
}

function proceedToCheckout() {
    if (currentItems.length === 0) {
        alert('Please add at least one item');
        return;
    }
    document.getElementById('addItemSection').style.display = 'none';
    document.getElementById('paymentSection').style.display = 'block';
}

function selectCash() {
    document.getElementById('cashSection').style.display = 'block';
    document.getElementById('amountReceived').focus();
    document.getElementById('amountReceived').oninput = calculateChange;

    // Display total amount for reference
    document.getElementById('totalAmount').textContent = grandTotal.toFixed(2);
}

function calculateChange() {
    amountReceived = parseFloat(document.getElementById('amountReceived').value) || 0;
    changeAmount = amountReceived - grandTotal;

    const display = document.getElementById('changeDisplay');
    if (changeAmount >= 0) {
        display.style.display = 'block';
        display.innerHTML = `Give back <strong>${changeAmount.toFixed(2)}</strong> to customer`;
    } else {
        display.style.display = 'none';
    }
}

function selectUPI() {
    document.getElementById('cashSection').style.display = 'none';

    // Store pending order in localStorage
    localStorage.setItem('pendingOrder', JSON.stringify({
        orderId: orderId,
        currentItems: currentItems,
        grandTotal: grandTotal
    }));

    // Redirect to UPI payment page
    window.location.href = '/upi-payment';
}

function completeOrder() {
    if (document.getElementById('cashSection').style.display !== 'none' && amountReceived < grandTotal) {
        alert('Amount received is less than total');
        return;
    }

    const order = {
        orderId: orderId,
        items: currentItems.map(item => ({
            product: item.product,
            quantity: item.quantity,
            subtotal: item.subtotal
        }))
    };

    fetch('/api/orders', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(order)
    })
    .then(response => response.json())
    .then(savedOrder => {
        document.getElementById('paymentSection').style.display = 'none';
        document.getElementById('successSection').style.display = 'block';

        let msg = `Order ID: ${savedOrder.orderId}<br>Total: ${savedOrder.total.toFixed(2)}`;
        if (changeAmount > 0) {
            msg += `<br>Change given back: ${changeAmount.toFixed(2)}`;
        }
        document.getElementById('successMessage').innerHTML = msg;
    })
    .catch(error => {
        alert('Error submitting order: ' + error.message);
    });
}

function startNewOrder() {
    location.reload();
}