<?php

$container = $app->getContainer();

$container['config'] = function ($container) use ($config) {
	return $config;
};

$container['view'] = function ($container) {
	$dir = dirname(__DIR__);
	$view = new \Slim\Views\Twig($dir . '/app/views', [
		'cache' => false //$dir . 'tmp/cache'
	]);

	// Instantiate and add Slim specific extension
	$basePath = rtrim(str_ireplace('index.php', '', $container['request']->getUri()->getBasePath()), '/');
	$view->addExtension(new Slim\Views\TwigExtension($container['router'], $basePath));

	return $view;
};

$container['guzzle'] = function ($container) {
	return new GuzzleHttp\Client();
};

$container['minecraft'] = function ($container) {
	return new App\MinecraftServerQuery($container,
		[
			'host' => $container->config['minecraft']['host'],
			'port' => $container->config['minecraft']['port']
		]);
};
