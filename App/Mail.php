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
		$this->Mail->Host = getenv("PHP_MAILER_HOST"); 					
		$this->Mail->SMTPAuth = true;                               	
		$this->Mail->Username = getenv('PHP_MAILER_USERNAME');          
		$this->Mail->Password = getenv('PHP_MAILER_PASSWORD');     
		$this->Mail->Port = getenv('PHP_MAILER_PORT'); 
		$this->Mail->setFrom(getenv('PHP_MAILER_FROM_EMAIL'), getenv('PHP_MAILER_FROM_NAME'));

    }


	public function sendMail($adress, $subject, $body, $attachment = null)
	{
		try {
			$this->Mail->addAddress($adress); // Adresse
			$this->Mail->isHTML(true);                                  // Set email format to HTML
			$this->Mail->Subject = $subject;
			$this->Mail->Body    = $body;
			if(!is_null($attachment)) $this->Mail->addAttachment($attachment);
			$this->Mail->send();
			return true;
		} catch (Exception $e) {
			return false;
		}
	}

}