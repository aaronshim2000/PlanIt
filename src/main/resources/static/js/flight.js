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

    var country = "CA";
    var currency = document.getElementById("currency").value; 
    var language = "en-US";
    var origin = document.getElementById("origin").value;
    var destination = document.getElementById("destination").value;
    var outboundDate = document.getElementById("outboundDate").value;

    var api_url = "https://skyscanner-skyscanner-flight-search-v1.p.rapidapi.com/apiservices/browsequotes/v1.0/"
        + country + "/" + currency + "/" + language + "/" + origin + "/" + destination + "/" + outboundDate;
    fetch(api_url, {
	"method": "GET",
	"headers": {
		"x-rapidapi-key": "1ce8798a48mshc81728a90222354p15a897jsn5a15b70589cc",
		"x-rapidapi-host": "skyscanner-skyscanner-flight-search-v1.p.rapidapi.com"
	}
    })
    .then(async response => {
        const data = await response.json();   
        console.log(data);
        buildTable(data);
    })
    .catch(err => {
        console.error(err);
    });
}

function buildTable(data)
{
    var table = document.getElementById("tableBody");

    table.innerHTML = "";

    for (var i = 0; i < data.Quotes.length; i++)
    {
        var quote = data.Quotes[i];

        var outboundCarrierId = quote.OutboundLeg.CarrierIds[0];
        var departureDate = quote.OutboundLeg.DepartureDate.slice(0,10);

        var outCarrierName;

        for (var j = 0; j < data.Carriers.length; j++)
        {
            var carrier = data.Carriers[j];

            if (outboundCarrierId == carrier.CarrierId)
                outCarrierName = carrier.Name;
        }

        var row = `<tr id=row${i}>
                        <td id=price${i}>${quote.MinPrice} ${data.Currencies[0].Code}</td>
                        <td id=outCarrier${i}>${outCarrierName}</td>   
                        <td id=departureDate${i}>${departureDate}</td>
                        <td>
                            <button class=save onclick="saveRow(${i})">Save</button>
                        </td>
                    </tr>`;
        table.innerHTML += row;
    }
}

function saveRow(i)
{
    var table = document.getElementById("tableBody");

    var price = document.getElementById("price" + i).value;
    var origin = document.getElementById("origin").value;
    var destination = document.getElementById("destination").value;
    var outboundDate = document.getElementById("departureDate" + i).value;
    var outCarrier = document.getElementById("outCarrier" + i).value;

    var rows = 
        `<input type="text" value="${price}" th:field=*{price}>
        <input type="text" value="${origin}" th:field=*{origin}>
        <input type="text" value="${destination}" th:field=*{destination}>
        <input type="text" value="${outboundDate}" th:field=*{outboundDate}>
        <input type="text" value="${outCarrier}" th:field=*{airline}>`

    table.innerHTML += rows;

    return true;
}