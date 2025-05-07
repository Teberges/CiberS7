const express = require('express');
const session = require('express-session');
const bodyParser = require('body-parser');
const csrf = require('csurf');
const path = require('path');
const crypto = require('crypto');
const MessageController = require('./controllers/messageController');
const encryptionUtil = require('./utils/encryption');

const app = express();
const messageController = new MessageController(encryptionUtil);

app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());
app.use(
  session({
    secret: 'your-secret-key',
    resave: false,
    saveUninitialized: true,
  })
);

const csrfProtection = csrf();
app.use(csrfProtection);

app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');

app.get('/', (req, res) => {
  res.render('chat', { csrfToken: req.csrfToken(), messages: messageController.getAllMessages() });
});

app.post('/send', (req, res) => {
  const { message } = req.body;

  // Criptografar a mensagem
  const encrypted = encryptionUtil.encryptMessage(message);

  // Descriptografar a mensagem
  const decrypted = encryptionUtil.decryptMessage(encrypted.encryptedData, encrypted.iv);

  // Retornar os resultados
  res.status(200).json({
    originalMessage: message,
    encryptedMessage: encrypted.encryptedData,
    decryptedMessage: decrypted,
  });
});

app.post('/simulate-encryption', (req, res) => {
  const message = "This is a secure message.";
  const encrypted = encryptionUtil.encryptMessage(message);
  const decrypted = encryptionUtil.decryptMessage(encrypted.encryptedData, encrypted.iv);

  let error = null;
  try {
    const wrongKey = crypto.randomBytes(32); // Chave incorreta
    const decipher = crypto.createDecipheriv(
      'aes-256-cbc',
      wrongKey,
      Buffer.from(encrypted.iv, 'hex')
    );
    let wrongDecrypted = decipher.update(encrypted.encryptedData, 'hex', 'utf8');
    wrongDecrypted += decipher.final('utf8');
  } catch (err) {
    error = err.message;
  }

  res.json({ encrypted, decrypted, error });
});

app.use((err, req, res, next) => {
  if (err.code === 'EBADCSRFTOKEN') {
    res.status(403).send('Form tampered with.');
  } else {
    next(err);
  }
});

const PORT = process.env.PORT || 3001;
app.listen(PORT, () => {
  console.log(`Server running on http://localhost:${PORT}`);
});