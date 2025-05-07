const express = require('express');
const csrf = require('csurf');
const MessageController = require('../controllers/messageController');

const router = express.Router();
const messageController = new MessageController();

const csrfProtection = csrf();

router.post('/send', csrfProtection, messageController.sendMessage.bind(messageController));

router.get('/messages', messageController.getMessages.bind(messageController));

module.exports = router;