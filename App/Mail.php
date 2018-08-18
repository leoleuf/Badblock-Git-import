<?php

namespace App;

use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;

class Mail
{
	private $Mail;

	function __construct($container) {
		$this->Mail = new PHPMailer();
		$this->Mail->SMTPDebug = 2;  
		$this->Mail->Host = 'tls://mail.badblockmail.fr'; 					// Host
		$this->Mail->SMTPAuth = true;                               		// Pour permettre l'authentificationgit 
		$this->Mail->Username = 'no-reply@badblockmail.fr';                 // SMTP username
		$this->Mail->Password = 'sivdgfmpyh2nsxfhyu9600y6tomobu5q';                           // SMTP password
		$this->Mail->SMTPSecure = 'tls';                            // Enable TLS encryption, `ssl` also accepted
		$this->Mail->Port = 587; 
		$this->Mail->setFrom('from@badblockmail.fr', 'BadBlock');

    }


	public function sendMail($adress, $subject, $body)
	{
		try {
            $this->Mail->addAddress($adress); // Adresse
			$this->Mail->isHTML(true);                                  // Set email format to HTML
			$this->Mail->Subject = $subject;
			$this->Mail->Body    = $body;
			$this->Mail->send();
			echo "send ok";
		} catch (Exception $e) {
			echo 'Message could not be sent. Mailer Error: ', $e;
		}
	}

}