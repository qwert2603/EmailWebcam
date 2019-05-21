import imaplib
import base64
import os
import email

mail = imaplib.IMAP4_SSL('imap.mail.ru')
mail.login('email', 'psw')
mail.select("photo")

result, data = mail.search(None, "ALL")
 
ids = data[0]
id_list = ids.split()

dnld_dir = 'dnld'
os.makedirs(dnld_dir, exist_ok=True)

for email_id in id_list:
    result, data = mail.fetch(email_id, "(RFC822)")
     
    raw_email = data[0][1]
    raw_email_string = raw_email.decode('utf-8')
    email_message = email.message_from_string(raw_email_string)

    for part in email_message.walk():
        if part.get_content_maintype() == 'multipart':
            continue
        if part.get('Content-Disposition') is None:
            continue
        fileName = part.get_filename()
        #fileName = base64.b64decode(fileName[10:-2])
        #fileName = str(fileName, "UTF-8")
        filePath = '{}/{}'.format(dnld_dir, fileName)
        fp = open(filePath, 'wb')
        fp.write(part.get_payload(decode=True))
        fp.close()
        print("downloaded: {}".format(fileName))

