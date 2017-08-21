<?php

$container = $app->getContainer();

$container['config'] = function ($container) use ($config) {
	return $config;
};

$container['session'] = function ($container) use ($config) {
	return new \App\Session();
};

$container['log'] = function ($container) {
	$log = new Monolog\Logger('badblock-website');
	$log->pushHandler(new Monolog\Handler\StreamHandler($container->config['log']['path'], $container->config['log']['level']));

	if ($container->config['log']['discord']){
		$log->pushHandler(new App\MonologDiscordHandler($container->guzzle, $container->config['log']['level']));
	}

	return $log;
};

$container['view'] = function ($container) use ($app) {
	$dir = dirname(__DIR__);
	$view = new \Slim\Views\Twig($dir . '/App/views', [
		'cache' => false //$dir . 'tmp/cache' OR '../tmp/cache'
	]);
	$twig = $view->getEnvironment();
	$twig->addExtension(new \App\TwigExtension());

	//global variables
	$twig->addGlobal('forum_url', $container['config']['forum_url']);
	$twig->addGlobal('current_url', $_SERVER['REQUEST_URI']);
	$twig->addGlobal('ts3_query', $container['config']['ts3_query']);

	// Instantiate and add Slim specific extension
	$basePath = rtrim(str_ireplace('index.php', '', $container['request']->getUri()->getBasePath()), '/');
	$view->addExtension(new Slim\Views\TwigExtension($container['router'], $basePath));

	return $view;
};

$container['guzzle'] = function ($container) {
	return new GuzzleHttp\Client();
};

$container['mysql'] = function ($container) {
	$pdo = new \Simplon\Mysql\PDOConnector(
		$container->config['mysql']['host'], // server
		$container->config['mysql']['user'],     // user
		$container->config['mysql']['password'],      // password
		$container->config['mysql']['database']   // database
	);

	$pdoConn = $pdo->connect('utf8', []); // charset, options

	$dbConn = new \Simplon\Mysql\Mysql($pdoConn);
	return $dbConn;
};

$container['mongo'] = function ($container) {
	return new \MongoDB\Client(
		'mongodb://node02-dev.cluster.badblock-network.fr/',
		[
			'username' => 'dev',
			'password' => 'VAQyVRregUwde5QUP7CVv15tZ7S5OwyC',
			'authSource' => 'badblock-website'
		]
    );
};

$container['minecraft'] = function ($container) {
	return new App\MinecraftServerQuery($container,
		[
			'host' => $container->config['minecraft']['host'],
			'port' => $container->config['minecraft']['port']
		]);
};

$container['xenforo'] = function ($container) {
	return new App\XenForo($container, [
		'endpoint' => $container->config['xenforo_api']['endpoint'],
		'hash' => $container->config['xenforo_api']['hash']
	]);
};

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

$container['notFoundHandler'] = function ($container) {
	return new \App\NotFoundHandler($container->get('view'), function ($request, $response) use ($container) {
		return $container['response']
			->withStatus(404);
	});
};