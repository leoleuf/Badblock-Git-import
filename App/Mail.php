<?php

namespace App;

use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\SMTP;
use PHPMailer\PHPMailer\Exception;

/**
 *  Class Mail
 *  Permet de créer et envoyer un mail.
 */

class Mail
{
	/**
	 * @var PHPMailer données du mail
	 */
	private $Mail;

	/**
	 * @param container $container Container utilisé
	 */
	function __construct($container) {
		$this->Mail = new PHPMailer();
		$this->Mail->isSMTP();
		$this->Mail->SMTPDebug = 0;
		$this->Mail->Host = getenv("PHP_MAILER_HOST"); 					
		$this->Mail->SMTPAuth = true;                               	
		$this->Mail->Username = getenv('PHP_MAILER_USERNAME');          
		$this->Mail->Password = getenv('PHP_MAILER_PASSWORD');
        $this->Mail->SMTPSecure = 'tls';
        $this->Mail->CharSet = 'UTF-8';
        $this->Mail->Port = getenv('PHP_MAILER_PORT');
		$this->Mail->setFrom(getenv('PHP_MAILER_FROM_EMAIL'), getenv('PHP_MAILER_FROM_NAME'));

    }

	/**
	 *  @param string $adress Adresse mail du destinataire
	 *  @param string $subject Sujet du mail
	 *  @param string $body Contenue HTML du mail
	 *  @param string $attachment Ficher envoyer par mail
 	*/
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