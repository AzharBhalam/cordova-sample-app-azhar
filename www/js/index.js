/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

// Wait for the deviceready event before using any of Cordova's device APIs.
// See https://cordova.apache.org/docs/en/latest/cordova/events/events.html#deviceready

/*var rzpOptions = {
    key: "rzp_test_1DP5mmOlF5G5ag",
    amount: "2000",
    currency: "INR",
    prefill: {
            contact: "9928815231",
            email: "azhar.ali@razorpay.com"
        },
    name: "Merchant Name",
    description: "Purchase Description",
    image: "https://i.imgur.com/n5tjHFD.png",
    handler: function (response){
        alert(response.razorpay_payment_id);
    },
    notes: {
        address: "Cordova plugin for UPI turbo"
    },
    theme: {
        color: "#F37254"
    },
    send_sms_hash: true,
    retry: {
        enabled: false,
        max_count: 4
    },
    disable_redesign_v15: false,
    experiments.upi_turbo: true,
    ep: "https://api-web-turbo-upi.ext.dev.razorpay.in/test/checkout.html"
};*/

var rzpOptions = {
    key: "rzp_test_0wFRWIZnH65uny",
    amount: 2000,
    currency: "INR",
    name: "Merchant Name",
    description: "Purchase Description",
    image: "https://i.imgur.com/n5tjHFD.png",
    handler: function (response){
        alert(response.razorpay_payment_id);
    },
    prefill: {
        contact: "9928815231",
        email: "azhar.ali@razorpay.com"
    },
    notes: {
        address: "Cordova plugin for UPI turbo"
    },
    theme: {
        color: "#F37254"
    },
    experiments:{
    upi_turbo: true
    },
    send_sms_hash: true,
    retry: {
         enabled: false,
         max_count: 4
    },
    disable_redesign_v15: false,
    ep: "https://api-web-turbo-upi.ext.dev.razorpay.in/test/checkout.html"
};

var successCallback = function(payment_id) {
  console.log("payment_id : "+ payment_id)
  //handle success
  alert('payment_id: ' + payment_id)
};

var cancelCallback = function(error) {
 console.log("error.description : "+ error.description)
 console.log("error.code : "+ error.code)
  alert(error.description + ' (Error '+error.code+')')
};

var app = {
    // Application Constructor
    initialize: function() {
        this.bindEvents();
    },
    // Bind Event Listeners
    //
    // Bind any events that are required on startup. Common events are:
    // 'load', 'deviceready', 'offline', and 'online'.
    bindEvents: function() {
        document.addEventListener('deviceready', this.onDeviceReady, false);
    },
    // deviceready Event Handler
    //
    // The scope of 'this' is the event. In order to call the 'receivedEvent'
    // function, we must explicitly call 'app.receivedEvent(...);'
    onDeviceReady: function() {
     console.log('Running cordova-' + cordova.platformId + '@' + cordova.version);
        document.getElementById('deviceready').classList.add('ready');

    // Get a reference to the button element by its id
        var btnPay = document.getElementById("btnPay");


        // Add a click event listener to the button
        btnPay.addEventListener("click", function () {
            // Display an alert when the button is clicked
            alert("Awesome, button is Clicked! Now next step is to integrate the latest checkout SDK to explore the functionality");
        });
        app.addRZPEventListener();
    },

    addRZPEventListener: function() {
        document.getElementById('rzp-open').addEventListener('click', function(event) {
            RazorpayCheckout.open(rzpOptions, successCallback, cancelCallback);
            event.preventDefault();
        });

        document.getElementById('rzp-link-new-acc').addEventListener('click', function(event) {
                    alert("coool");
                    RazorpayCheckout.linkNewUPIAccount(rzpOptions, successCallback, cancelCallback);
                    event.preventDefault();
                })
    }
};

app.initialize();
