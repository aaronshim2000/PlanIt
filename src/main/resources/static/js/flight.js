document.addEventListener('DOMContentLoaded', () => 
{
    const origin = document.getElementById("origin");
    const destination = document.getElementById("destination");

    fetch('https://restcountries.eu/rest/v2/all').then(res => {
            return res.json();
        }).then(data => {
            let output = `<option>Country</option>`;
            data.forEach(country => {
                output += `<option value="${country.alpha2Code}">${country.name}</option>`;
            })

            origin.innerHTML = output;
            destination.innerHTML = output;
        }).catch(err => {
            console.error(err);
        })
});

async function getResponse()
{
    // Change url parameters given form data
    // format: url/countryOfUser/Currency/language/origin/destination/outbound-date/inbound-date
    // example url = "https://skyscanner-skyscanner-flight-search-v1.p.rapidapi.com/apiservices/browsequotes/v1.0/US/USD/en-US/SFO-sky/JFK-sky/2019-09-01?inboundpartialdate=2019-12-01"

    var country = "US"; // idk if this matters
    var currency = getElementById("currency").innerHTML; 
    var language = "en-US";
    var origin = getElementById("origin").innerHTML;
    var destination = getElementById("destination").innerHTML;
    var outboundDate = getElementById("outboundDate").innerHTML;
    var inboundDate = getElementById("inboundDate").innerHTML;

    var api_url = "https://skyscanner-skyscanner-flight-search-v1.p.rapidapi.com/apiservices/browsequotes/v1.0/"
        + country + "/" + currency + "/" + language + "/" + origin + "/" + destination + "/" + outboundDate + "?inboundpartialdate=" + inboundDate;
    fetch(api_url, {
	"method": "GET",
	"headers": {
		"x-rapidapi-key": "1ce8798a48mshc81728a90222354p15a897jsn5a15b70589cc",
		"x-rapidapi-host": "skyscanner-skyscanner-flight-search-v1.p.rapidapi.com"
	}
    })
    .then(response => {
        const data = response.json;
        // Manipulate the data from json file
    })
    .catch(err => {
        console.error(err);
    });
}