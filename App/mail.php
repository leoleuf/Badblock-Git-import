<?php

use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;

//Load Composer's autoloader
require '../vendor/autoload.php';



class Mail
{
	private $Mail;

	function __construct() {
		$this->Mail = new PHPMailer();
		$this->Mail->SMTPDebug = 2;  
		$this->Mail->Host = 'tls://mail.badblockmail.fr';
		$this->Mail->SMTPAuth = true;                               // Enable SMTP authentication
		$this->Mail->Username = 'no-reply@badblockmail.fr';                 // SMTP username
		$this->Mail->Password = 'sivdgfmpyh2nsxfhyu9600y6tomobu5q';                           // SMTP password
		$this->Mail->SMTPSecure = 'tls';                            // Enable TLS encryption, `ssl` also accepted
		$this->Mail->Port = 587; 
		$this->Mail->setFrom('from@example.com', 'Mailer');
		$this->Mail->addAddress('gastbob40@gmail.com');
		echo "construct ok\n";
    }


	public function sendMail()
	{
		try {
			$this->Mail->isHTML(true);                                  // Set email format to HTML
			$this->Mail->Subject = 'Here is the subject';
			$this->Mail->Body    = 'This is the HTML message body <b>in bold!</b>';
			$this->Mail->send();
			echo "send ok";
		} catch (Exception $e) {
			echo 'Message could not be sent. Mailer Error: ', $mail->ErrorInfo;
		}
	}

}