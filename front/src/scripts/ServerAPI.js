
function chat(msg, callback) {
    fetch('/server-api/chat/text?msg=' + msg, {
        method: 'GET',
    }).then((response) => response.text())
        .then((data) =>  {
            callback(data)
        });
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

export default {
    chat: chat,
    save: save,
    load: load
}