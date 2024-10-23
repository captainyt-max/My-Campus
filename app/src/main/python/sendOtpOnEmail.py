import smtplib
from email.mime.text import MIMEText
import random



def send_email_otp(receiver_email):
    sender_email = "pocketcampusngp@gmail.com"
    password = "xvqkxnpbkdpfnknw"

    otp = random.randint(100000, 999999)

    message = MIMEText(f"OTP to verify your email is : {otp}\n\n\nNote : This is a computer generated email, please don't reply to it")
    message['Subject'] = 'Pocket campus email Verification'
    message['From'] = 'Pocket Campus'
    message['To'] = receiver_email

    try:
        # Using Outlook SMTP server
        #server = smtplib.SMTP('smtp-mail.outlook.com', 587)
        server = smtplib.SMTP('smtp.gmail.com', 587)
        server.starttls()
        server.login(sender_email, password)
        server.sendmail(sender_email, receiver_email, message.as_string())
        print("OTP sent successfully!")
    except Exception as e:
        print(f"Failed to send OTP: {e}")
    finally:
        server.quit()

    return otp