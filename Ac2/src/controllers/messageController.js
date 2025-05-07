class MessageController {
    constructor(encryptionUtil) {
        this.encryptionUtil = encryptionUtil;
        this.messages = [];
    }

    sendMessage(req, res) {
        const { message } = req.body;
        const encryptedMessage = this.encryptionUtil.encryptMessage(message);
        this.messages.push({ sender: 'User', content: encryptedMessage.encryptedData, iv: encryptedMessage.iv });
        res.status(200).json({
            originalMessage: message,
            encryptedMessage: encryptedMessage.encryptedData,
            decryptedMessage: this.encryptionUtil.decryptMessage(encryptedMessage.encryptedData, encryptedMessage.iv),
        });
    }

    getAllMessages() {
        return this.messages.map(msg => ({
            sender: msg.sender,
            content: this.encryptionUtil.decryptMessage(msg.content, msg.iv),
        }));
    }
}

module.exports = MessageController;