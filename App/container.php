<?php

$container = $app->getContainer();

$container['config'] = function ($container) use ($config) {
	return $config;
};

$container['view'] = function ($container) {
	$dir = dirname(__DIR__);
	$view = new \Slim\Views\Twig($dir . '/App/Views', [
		'cache' => false //$dir . 'tmp/cache'
	]);
	$twig = $view->getEnvironment();
	$twig->addExtension(new \App\TwigExtension());
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

$container['xenforo'] = function ($container) {
	return new App\Xenforo($container, [
		'endpoint' => $container->config['xenforo_api']['endpoint'],
		'hash' => $container->config['xenforo_api']['hash']
	]);
};