
function chat(msg, callback, finish) {
    const eventSource = new EventSource(`/server-api/ai/stream?msg=` + msg);
    eventSource.onmessage = function(event) {
        const data = JSON.parse(event.data)
        callback(data.result.output.content,null)
        if (data.result.metadata.finishReason==='unknown'){
            eventSource.close()
            finish()
        }
    }
}

function save(chatId, data, callback) {
    const headers = new Headers();
    // headers.append('Authorization', store.state.userId);
    // headers.append('Authorization', "jyk");
    headers.append('Content-Type', 'application/json')
    fetch('/server-api/ai/save/'+chatId, {
        method: 'POST',
        headers: headers,
        body: JSON.stringify({'data': JSON.stringify(data)}),
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

function load(chatId,callback) {
    fetch('/server-api/ai/load/' + chatId, {
        method: 'GET',
    }).then((response) => {
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        response.json()
            .then((data) =>  {
                callback(data.data)
            });
    })

}

function clear(chatId,callback) {
    fetch('/server-api/ai/clear/' + chatId, {
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

function loadAll(callback) {
    fetch('/server-api/ai/loadAll', {
        method: 'GET',
    }).then(response=> {
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        return response.json()

    }).then(data => {
        callback(data)
    })
}

export default {
    chat: chat,
    save: save,
    load: load,
    clear: clear,
    loadAll: loadAll,
}
