<?php

return [
	'shop' => [
		'payways' => [
			'paypal' => [
				'coef' => 1,
				'table' => [
					5 => 5,
					10 => 10,
					15 => 15,
                    20 => 20
				]
			],
            'starpass' => [
                'coef' => 0,
                'table' => [
                    5 => 5,
                    10 => 10,
                    15 => 15,
                    20 => 20,
                    50 => 50
                ]
            ],
            'paysafecard' => [
                'coef' => 0,
                'table' => [
                    5 => 5,
                    10 => 10,
                    15 => 15,
                    20 => 20
                ]
            ]
		],
        'offer' => [

        ]
	]
];