function getPayPalForm() {
    return `<label for="payment-form">Enter PayPal account details:</label>
             <form id="payment-form">
              <div class="form-group">
                <label for="email">Email:</label>
                <input required minlength="6" type="email" class="form-control" id="paypal-email" placeholder="email">
              </div>
              <div class="form-group">
                <label for="password">Password:</label>
                <input required minlength="6" type="password" class="form-control" id="paypal-password" placeholder="password">
              </div>
              <button type="submit" class="btn btn-primary">Pay with PayPal</button>
            </form>`
}

function getCreditCardFrom() {
    return `<form id="payment-form">
              <label for="card-details">Enter Credit Card details:</label>
              <div class="form-group" id="card-details">
                <input type="text" class="form-control" id="card-number" placeholder="1234 1234 1234 1234">
                <input type="number" class="form-control" id="expiration-month" placeholder="MM">
                <input type="number" class="form-control" id="expiration-year" placeholder="YY">
                <input type="text" class="form-control" id="cvv" placeholder="123">
              </div>
              <div class="form-group">
                <label for="card-holder">Name on card:</label>
                <input type="text" class="form-control" id="card-owner" placeholder="name on card">
              </div>
              <button type="submit" class="btn btn-primary">Pay with Credit Card</button>
            </form>`
}

function initPaymentSelector() {
    const paymentSelector = document.querySelector("#payment-selector");
    paymentSelector.addEventListener("submit", e => {
        e.preventDefault();
        const paymentMethod = document.querySelector('input[name="payment-method"]:checked').value
        if (paymentMethod != null) {
            const paymentDiv = document.querySelector("#payment");
            if (paymentMethod === "paypal") {
                paymentDiv.innerHTML = getPayPalForm();
            } else {
                paymentDiv.innerHTML = getCreditCardFrom();
            }
        }
    })
}

initPaymentSelector();