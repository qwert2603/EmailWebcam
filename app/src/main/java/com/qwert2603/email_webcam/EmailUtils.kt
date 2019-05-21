package com.qwert2603.email_webcam

import java.io.File
import java.util.*
import javax.activation.DataHandler
import javax.activation.FileDataSource
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart

object EmailUtils {

    fun sendEmail(file: File) {
        val session = createSessionObject()
        val message = createMessage(session, file)
        Transport.send(message)
    }

    private fun createSessionObject(): Session {
        val properties = Properties()
        properties["mail.smtp.auth"] = "true"
        properties["mail.smtp.starttls.enable"] = "true"
        properties["mail.smtp.host"] = "smtp.gmail.com"
        properties["mail.smtp.port"] = "587"

        return Session.getInstance(properties, object : javax.mail.Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(BuildConfig.SENDER_EMAIL, BuildConfig.SENDER_PASSWORD)
            }
        })
    }

    private fun createMessage(session: Session, file: File): Message {
        val message = MimeMessage(session)
        message.setFrom(InternetAddress(BuildConfig.SENDER_EMAIL))
        message.addRecipient(Message.RecipientType.TO, InternetAddress(BuildConfig.RECEIVER_EMAIL))
        message.subject = "photo"

        val multipart = MimeMultipart()

        val messageBodyPart = MimeBodyPart()
        val source = FileDataSource(file)
        messageBodyPart.dataHandler = DataHandler(source)
        messageBodyPart.fileName = file.name
        multipart.addBodyPart(messageBodyPart)

        message.setContent(multipart)

        return message
    }
}