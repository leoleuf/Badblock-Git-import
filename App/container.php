<?php

use Symfony\Bridge\Twig\Extension\TranslationExtension;
use Symfony\Component\Translation\Loader\PhpFileLoader;
use Symfony\Component\Translation\MessageSelector;
use Symfony\Component\Translation\Translator;

$container = $app->getContainer();

$container['config'] = function ($container) use ($config) {
	return $config;
};

$container['session'] = function ($container) use ($config) {
	return new \App\Session();
};

$container['codepromo'] = array(

);

$container['redis_client'] = function ($container) {
	$client = new Predis\Client(
		[
			'scheme' => 'tcp',
			'host' => $container->config['redis']['host'],
			'port' => $container->config['redis']['port'],
		], [
			'parameters' => [
				'password' => $container->config['redis']['password'],
			]
		]
	);

	return $client;
};

$container['redis'] = function ($container) {
	return new \App\Redis($container->redis_client);
};

$container['mail'] = function ($container) {
    return new App\Mail($container);
};

$container['log'] = function ($container) {
	return new App\DiscordHandler($container);
};

$container['flash'] = function () {
	return new Slim\Flash\Messages();
};


$container['translator'] = function ($container) {
	// First param is the "default language" to use.
	$translator = new Translator('fr_FR', new MessageSelector());
	// Set a fallback language incase you don't have a translation in the default language
	$translator->setFallbackLocales(['fr_FR']);
	// Add a loader that will get the php files we are going to store our translations in
	$translator->addLoader('php', new PhpFileLoader());

	// Add language files here
	$translator->addResource('php', '../App/lang/fr_FR.php', 'fr_FR');
	$translator->addResource('php', '../App/lang/en_EN.php', 'en_EN');

	return $translator;
};

$container['view'] = function ($container) use ($app) {
	$dir = dirname(__DIR__);
	$view = new \Slim\Views\Twig($dir . '/App/views', [
		'cache' => false //$dir . 'tmp/cache' OR '../tmp/cache'
	]);
	$twig = $view->getEnvironment();
	$twig->addExtension(new \App\TwigExtension($container));

    //global variables
	$twig->addGlobal('forum_url', $container['config']['forum_url']);
	$twig->addGlobal('current_url', $_SERVER['REQUEST_URI']);
	$twig->addGlobal('ts3_query', $container['config']['ts3_query']);

	if ($container['session']->exist('user')) {
		$twig->addGlobal('user', $container['session']->get('user'));
	} elseif ($container['session']->exist('recharge')) {
		$twig->addGlobal('recharge', $container['session']->get('recharge'));
	}

	// Instantiate and add Slim specific extension
	$basePath = rtrim(str_ireplace('index.php', '', $container['request']->getUri()->getBasePath()), '/');
	$view->addExtension(new Slim\Views\TwigExtension($container['router'], $basePath));
	//translator helper
	$view->addExtension(new TranslationExtension($container['translator']));

	return $view;
};

$container['guzzle'] = function ($container) {
	$client = new GuzzleHttp\Client();

	return $client;
};

$container['mysql_forum'] = function ($container) {
	$pdo = new \Simplon\Mysql\PDOConnector(
		$container->config['mysql_forum']['host']. ':' . $container->config['mysql_forum']['port'], // server
		$container->config['mysql_forum']['user'],     // user
		$container->config['mysql_forum']['password'],      // password
		$container->config['mysql_forum']['database']   // database
	);

	$pdoConn = $pdo->connect('utf8', []); // charset, options

	$dbConn = new \Simplon\Mysql\Mysql($pdoConn);

	return $dbConn;
};


$container['mysql_rankeds'] = function ($container) {
	$pdo = new \Simplon\Mysql\PDOConnector(
		$container->config['mysql_rankeds']['host']. ':' . $container->config['mysql_rankeds']['port'], // server
		$container->config['mysql_rankeds']['user'],     // user
		$container->config['mysql_rankeds']['password'],      // password
		$container->config['mysql_rankeds']['database']   // database
	);

	$pdoConn = $pdo->connect('utf8', []); // charset, options

	$dbConn = new \Simplon\Mysql\Mysql($pdoConn);

	return $dbConn;
};

$container['mysql_freebuild'] = function ($container) {
    $pdo = new \Simplon\Mysql\PDOConnector(
        $container->config['mysql_freebuild']['host']. ':' . $container->config['mysql_freebuild']['port'], // server
        $container->config['mysql_freebuild']['user'],     // user
        $container->config['mysql_freebuild']['password'],      // password
        $container->config['mysql_freebuild']['database']   // database
    );

    $pdoConn = $pdo->connect('utf8', []); // charset, options

    $dbConn = new \Simplon\Mysql\Mysql($pdoConn);

    return $dbConn;
};


$container['mysql_casier'] = function ($container) {
	$pdo = new \Simplon\Mysql\PDOConnector(
		$container->config['mysql_casier']['host']. ':' . $container->config['mysql_casier']['port'], // server
		$container->config['mysql_casier']['user'],     // user
		$container->config['mysql_casier']['password'],      // password
		$container->config['mysql_casier']['database']   // database
	);
	$pdoConn = $pdo->connect('utf8', []); // charset, options

	$dbConn = new \Simplon\Mysql\Mysql($pdoConn);

	return $dbConn;
};

$container['mysql_guardian'] = function ($container) {
	$pdo = new \Simplon\Mysql\PDOConnector(
		$container->config['mysql_guardian']['host'] . ':' . $container->config['mysql_guardian']['port'], // server
		$container->config['mysql_guardian']['user'],     // user
		$container->config['mysql_guardian']['password'],      // password
		$container->config['mysql_guardian']['database']   // database
	);

	$pdoConn = $pdo->connect('utf8', []); // charset, options

	$dbConn = new \Simplon\Mysql\Mysql($pdoConn);

	return $dbConn;
};

$container['mongoUltra'] = function ($container) {
    $client = new \MongoDB\Client(
        'mongodb://' . $container->config['mongo_ultra']['user'] . ":" . $container->config['mongo_ultra']['password'] . "@" . $container->config['mongo_ultra']['host'] . ":" . $container->config['mongo_ultra']['port'] . "/" . $container->config['mongo_ultra']['database']
    );

    return $client->selectDatabase($container->config['mongo_ultra']['database']);
};

$container['mongoServer'] = function ($container) {
    $client = new \MongoDB\Client(
        'mongodb://' . $container->config['mongo_dist']['user'] . ":" . $container->config['mongo_dist']['password'] . "@" . $container->config['mongo_dist']['host'] . ":" . $container->config['mongo_dist']['port'] . "/" . $container->config['mongo_dist']['database']
    );

    return $client->selectDatabase($container->config['mongo_dist']['database']);
};

$container['mongo'] = function ($container) {
	$client = new \MongoDB\Client(
		'mongodb://' . $container->config['mongo_local']['user'] . ":" . $container->config['mongo_local']['password'] . "@" . $container->config['mongo_local']['host'] . ":" . $container->config['mongo_local']['port'] . "/" . $container->config['mongo_local']['database']
	);

	return $client->selectDatabase($container->config['mongo_local']['database']);
};

$container['xenforo'] = function ($container) {
	return new App\XenForo($container, [
		'endpoint' => $container->config['xenforo_api']['endpoint'],
	]);
};

$container['paypal'] = function ($container) {
	return new \lefuturiste\PaypalExpressCheckout\Paypal(
		$container->config['paypal']['username'],
		$container->config['paypal']['password'],
		$container->config['paypal']['signature'],
		$container->config['paypal']['prod']
	);
};

$container['notFoundHandler'] = function ($container) {
	return new \App\NotFoundHandler($container->get('view'), function ($request, $response) use ($container) {
		return $container['response']
			->withStatus(404);
	});
};


$container['ladder'] = function ($container) {
	return new App\Ladder($container, [
		'ip' => $container->config['ladder']['ip'],
		'port' => $container->config['ladder']['port']
	]);
};

$container['teamspeak'] = function ($container) {
   return new App\Teamspeak($container, (object) [
        'ip' => $container->config['teamspeak']['ip'],
        'port' => $container->config['teamspeak']['port'],
        'username' => $container->config['teamspeak']['username'],
        'password' => $container->config['teamspeak']['password'],
        'query_port' => $container->config['teamspeak']['query_port']
    ]);
};


