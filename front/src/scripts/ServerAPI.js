
function chat(msg, callback) {
    const eventSource = new EventSource(`/server-api/chat/stream?msg=` + msg);
    eventSource.onmessage = function(event) {
        const data = JSON.parse(event.data);
        callback(data.result.output.content)
        if (data.result.metadata.finishReason==='unknown'){
            eventSource.close()
        }
    }
}

function save(data, callback) {
    const headers = new Headers();
    // headers.append('Authorization', store.state.userId);
    // headers.append('Authorization', "jyk");
    headers.append('Content-Type', 'application/json')
    fetch('/server-api/chat/save', {
        method: 'POST',
        headers: headers,
        body: JSON.stringify({'data': data}),
    }).then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.text();
    }).then(data => {
        callback(data, null)
    }).catch(error => {
        callback(null, error)
    });
}

function load(callback) {
    fetch('/server-api/chat/load', {
        method: 'GET',
    }).then((response) => response.json())
        .then((data) =>  {
            callback(data.data)
        });
}

function clear(callback) {
    fetch('/server-api/chat/clear', {
        method: 'DELETE',
    }).then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.text();
    }).then(data => {
        callback(data, null)
    }).catch(error => {
        callback(null, error)
    });
}


export default {
    chat: chat,
    save: save,
    load: load,
    clear: clear
}

// fetch('/server-api/chat/stream?msg=' + msg, {
//     method: 'GET',
// }).then((response) => response.text())
//     .then((data) =>  {
//         console.log(JSON.parse(data))
//         callback(data)
//         // const msg = data.slice(6,data.length+1)
//         // console.log(msg)
//     });
