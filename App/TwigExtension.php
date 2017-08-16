<?php

namespace App;

class TwigExtension extends \Twig_Extension
{

	public function getFilters()
	{
		return [
			new \Twig_SimpleFilter('markdown', [$this, 'markdown'], ['is_safe' => ['html']]),
			new \Twig_SimpleFilter('short', [$this, 'short']),
		];
	}

	public function getFunctions()
	{
		return [
			new \Twig_Function('short_str', [$this, 'short_str']),
			new \Twig_Function('getHoursFromDateTime', [$this, 'getHoursFromDateTime']),
			new \Twig_Function('timeago', [$this, 'timeago']),
		];
	}

	public function short_str($string, $long = 20)
	{
		if (strlen($string) > $long) {
			return substr($string, 0, $long) . '...';
		} else {
			return $string;
		}
	}

	public function markdown($value)
	{
		$Parsedown = new \Parsedown();

		return $Parsedown->text($value);
	}

	public function short($string)
	{
		return substr($string, 0, 20);
	}

	public function getHoursFromDateTime($date)
	{
		$dt = \DateTime::createFromFormat("Y-m-d H:i:s", $date);
		$hours = $dt->format('H:i');

		return $hours;
	}

	public function timeago($date)
	{
		$timeAgo = new \Westsworld\TimeAgo(NULL, 'fr');

		return ucfirst(
			$timeAgo->inWords(
				$date
			)
		);
	}

}