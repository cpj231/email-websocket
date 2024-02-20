const app = new Vue({
    el: '#app',
    data: {
        loggedIn: false,
        username: '',
        password: '',
        emails: []
    },
    methods: {
        login() {
            const socket = new SockJS('/websocket');
            const stompClient = Stomp.over(socket);
            stompClient.connect({}, () => {
                this.loggedIn = true;
                this.fetchEmails(stompClient);
                stompClient.subscribe('/topic/emails', (message) => {
                    this.emails.push(JSON.parse(message.body));
                });
            });
        },
        fetchEmails(stompClient) {
            // 调用后端API获取未读邮件
            // 为了简单起见，我们添加一些虚拟数据
            this.emails.push({ id: 1, subject: 'Test Email', content: 'This is a test email.', opened: false });
        },
        toggleEmail(email) {
            email.opened = !email.opened;
        }
    }
});
