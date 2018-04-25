<?php

namespace App\Commands;

use Slim\App;
use \Symfony\Component\Console\Command\Command;
use \Symfony\Component\Console\Input\InputInterface;
use \Symfony\Component\Console\Output\OutputInterface;
use \Symfony\Component\Console\Input\InputArgument;
use Symfony\Component\Console\Style\SymfonyStyle;


class MaintenanceCommand extends Command
{
	protected function configure()
	{
		$this->setName('maintenance')
			->setDescription('Execute maintenance')
			->addArgument('mode', InputArgument::REQUIRED, 'open|close Open or close maintenance mode');
	}

	protected function execute(InputInterface $input, OutputInterface $output)
	{
		$io = new SymfonyStyle($input, $output);

		switch ($input->getArgument('mode')){
			case 'open':
				chdir('public');
				//if maintenance mode is close
				if (file_exists('maintenance.php')){
					rename('index.php', '_index.php');
					rename('maintenance.php', 'index.php');

                    $data = array("username" => "Logger Site","content" => "[OK] Maintenance mode is now enabled ! <@&428275202166751236> ");

                    $curl = curl_init("https://canary.discordapp.com/api/webhooks/418434729084190732/DKbr0dN-PNwi260GP9lPjah3RqJFQ2uuRnfK11rifovdcxcsGSrjD-Og_LWKV0d4Pe2_");
                    curl_setopt($curl, CURLOPT_CUSTOMREQUEST, "POST");
                    curl_setopt($curl, CURLOPT_POSTFIELDS, json_encode($data));
                    curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
                    curl_exec($curl);

					$io->success('Maintenance mode is now enabled!');
				}else{
					$io->error('Maintenance mode is already enabled!');
				}
				break;

			case 'close':
				chdir('public');

				//if maintenance mode is open
				if (file_exists('_index.php')){
					rename('index.php', 'maintenance.php');
					rename('_index.php', 'index.php');

                    $data = array("username" => "Logger Site","content" => "[OK] Maintenance mode is now disabled ! <@&428275202166751236> ");

                    $curl = curl_init("https://canary.discordapp.com/api/webhooks/418434729084190732/DKbr0dN-PNwi260GP9lPjah3RqJFQ2uuRnfK11rifovdcxcsGSrjD-Og_LWKV0d4Pe2_");
                    curl_setopt($curl, CURLOPT_CUSTOMREQUEST, "POST");
                    curl_setopt($curl, CURLOPT_POSTFIELDS, json_encode($data));
                    curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
                    curl_exec($curl);

					$io->success('Maintenance mode is now disabled!');
				}else{
					$io->error('Maintenance mode is already disabled!');
				}
				break;

			default:
				$io->error('Invalid argument! Do "open" or "close"');
				break;
		}
	}
}