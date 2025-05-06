const express = require('express');
const router = express.Router();
const csrf = require('csrf');
const crypto = require('crypto');


const messages = [];
const users = {
  user1: { username: 'user1', password: 'password1' },
  user2: { username: 'user2', password: 'password2' },
};


function ensureAuthenticated(req, res, next) {
  if (!req.session.user) {
    return res.status(403).send('Unauthorized');
  }
  next();
}


router.post('/login', (req, res) => {
  const { username, password } = req.body;
  if (users[username] && users[username].password === password) {
    req.session.user = username;
    res.redirect('/dashboard');
  } else {
    res.status(401).send('Invalid credentials');
  }
});


router.post('/messages/send', ensureAuthenticated, csrf(), (req, res) => {
  const { recipient, message } = req.body;
  if (!users[recipient]) {
    return res.status(400).send('Recipient does not exist');
  }
  const encryptedMessage = crypto.createCipher('aes-128-cbc', 'secret-key').update(message, 'utf8', 'hex');
  messages.push({ sender: req.session.user, recipient, message: encryptedMessage });
  res.send('Message sent successfully');
});


router.get('/messages', ensureAuthenticated, (req, res) => {
  const userMessages = messages.filter(msg => msg.recipient === req.session.user);
  res.json(userMessages);
});

module.exports = router;


router.get('/dashboard', ensureAuthenticated, (req, res) => {
  const userMessages = messages.filter(msg => msg.recipient === req.session.user);
  res.render('dashboard', {
    username: req.session.user,
    balance: users[req.session.user].balance,
    csrfToken: req.csrfToken(),
    messages: userMessages.map(msg => ({
      sender: msg.sender,
      message: decryptMessage(msg.message), 
      timestamp: msg.timestamp,
    })),
  });
});


function decryptMessage(encryptedMessage) {
  const decipher = crypto.createDecipher('aes-128-cbc', 'secret-key');
  let decrypted = decipher.update(encryptedMessage, 'hex', 'utf8');
  decrypted += decipher.final('utf8');
  return decrypted;
}