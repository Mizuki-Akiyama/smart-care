import router from "@/router/index.js";

const headers = new Headers();
headers.append('Content-Type', 'application/json')
headers.append("Authorization", "Bearer " + localStorage.getItem("validUser"));

function chat(chatId, msg, callback, finish) {
    const eventSource = new EventSource(`/server-api/ai/stream?msg=` + msg + `&chatId=` + chatId + '&token=' + localStorage.getItem("validUser"));
    let messageCount = 0;
    let isFinished = false;

    eventSource.onmessage = function (event) {
        if (isFinished) return
        const data = JSON.parse(event.data)

        messageCount++;
        if (messageCount >= 4) {
            callback(data.result.output.text, null);
        }
        if (data.result.metadata.finishReason === 'stop') {
            eventSource.close()
            finish()
        }
    }

    return {
        abort: ()=>{
            if(!isFinished){
                isFinished = true
                eventSource.close()
                finish()
            }
        }
    }

}

function save(chatId, data, callback) {
    fetch('/server-api/ai/save/' + chatId, {
        method: 'POST',
        headers: headers,
        body: JSON.stringify({'data': JSON.stringify(data)}),
    }).then(response => {
        if (response.status === 401) {
            router.push('/error');
            throw new Error('未授权，请重新登录');
        }

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

function load(chatId, callback) {
    fetch('/server-api/ai/load/' + chatId, {
        method: 'GET',
        headers: headers,
    }).then((response) => {
        if (response.status === 401) {
            router.push('/error');
            throw new Error('未授权，请重新登录');
        }

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        response.json()
            .then((data) => {
                callback(data.data)
            });
    })

}

function clear(chatId, callback) {
    fetch('/server-api/ai/clear/' + chatId, {
        method: 'DELETE',
        headers: headers,
    }).then(response => {
        if (response.status === 401) {
            router.push('/error');
            throw new Error('未授权，请重新登录');
        }

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
        headers: headers,
    }).then(response => {
        if (response.status === 401) {
            router.push('/error');
            throw new Error('未授权，请重新登录');
        }

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        return response.json()

    }).then(data => {
        callback(data)
    })
}

function setTitle(chatId, title, callback) {
    fetch('/server-api/ai/setTitle?chatId=' + chatId + '&title=' + title, {
        method: 'POST',
        headers: headers,
        body: title,
    }).then(response => {
        if (response.status === 401) {
            router.push('/error');
            throw new Error('未授权，请重新登录');
        }

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        return response.text()
    }).then(() => {
        callback()
    })
}

function login(userId, password, callback) {
    fetch('/server-api/user/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            userId,
            password
        }),
    }).then(response => {
        if (!response.ok) {
            throw new Error(response.statusText)
        }

        return response.text()
    }).then((user) => {
        localStorage.setItem("validUser", JSON.parse(user).token)
        callback(user)
    })
}

function register(user, callback) {
    fetch('/server-api/user/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: user
    }).then(response => {
        if (!response.ok) {
            throw new Error(response.statusText)
        }

        return response.text()
    }).then(() => {
        callback()
    })
}

function getUserById(Id, callback) {
    fetch('/server-api/user/getUserById?id=' + Id, {
        method: 'GET',
        headers: headers,
    }).then(response => {
        if (response.status === 401) {
            router.push('/error');
            throw new Error('未授权，请重新登录');
        }

        if (!response.ok) {
            throw new Error(response.statusText)
        }
        return response.json()

    }).then((user) => {
        callback(user)
    })

}

function updateUser(user, callback) {
    fetch('/server-api/user/updateUser', {
        method: 'POST',
        headers: headers,
        body: JSON.stringify(user)
    }).then(response => {
        if (response.status === 401) {
            router.push('/error');
            throw new Error('未授权，请重新登录');
        }

        if (!response.ok) {
            throw new Error(response.statusText)
        }

        return response.text()
    }).then((user) => {
        console.log(user)
        callback(user)
    })
}


export default {
    chat: chat,
    save: save,
    load: load,
    clear: clear,
    loadAll: loadAll,
    setTitle: setTitle,
    login: login,
    register: register,
    getUserById: getUserById,
    updateUser: updateUser,
}
