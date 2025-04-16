const express = require('express');
const router = express.Router();

// Simulated Database
const users = {
  user1: { username: 'user1', password: 'password1', balance: 100 },
};

let currentUser = null;

// Login Page
router.get('/login', (req, res) => {
  res.render('login', { csrfToken: req.csrfToken() });
});

// Login Action
router.post('/login', (req, res) => {
  const { username, password } = req.body;
  if (users[username] && users[username].password === password) {
    req.session.user = username;
    res.redirect('/dashboard');
  } else {
    res.send('Invalid username or password');
  }
});

// Dashboard
router.get('/dashboard', (req, res) => {
  if (!req.session.user) {
    return res.redirect('/login');
  }
  res.render('dashboard', {
    username: req.session.user,
    balance: users[req.session.user].balance,
    csrfToken: req.csrfToken(),
  });
});

// Transfer Action
router.post('/transfer', (req, res) => {
  if (!req.session.user) {
    return res.status(403).send('Unauthorized');
  }
  const { amount } = req.body;
  const user = users[req.session.user];
  if (user.balance >= amount) {
    user.balance -= amount;
    res.send('Transfer successful');
  } else {
    res.send('Insufficient balance');
  }
});

module.exports = router;