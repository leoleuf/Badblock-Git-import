<?php
return [
    'paiement' => [
        [
            'name' => 'Carte Bancaire & PayPal',
            'image' => "http://localhost/dist/images/paypal.png",
            'class' => "paypal",
            'offer' => [
                0 => ["points" => 1500, "price" => 5],
                1 => ["points" => 3000, "price" => 10],
                2 => ["points" => 5400, "price" => 15],
                3 => ["points" => 7800, "price" => 20],
                4 => ["points" => 11400, "price" => 30],
                5 => ["points" => 18300, "price" => 50],
                6 => ["points" => 34200, "price" => 100]
            ]
        ],
        [
            'name' => 'PaySafeCard',
            'image' => "http://localhost/dist/images/paypal.png",
            'class' => "paysafecard",
            'offer' => [
                0 => ["points" => 1500, "price" => 5],
                1 => ["points" => 3000, "price" => 10],
                2 => ["points" => 5400, "price" => 15],
                3 => ["points" => 7800, "price" => 20],
                4 => ["points" => 11400, "price" => 30],
                5 => ["points" => 18300, "price" => 50],
                6 => ["points" => 34200, "price" => 100]
            ]
        ],
        [
            'name' => 'Internet +',
            'image' => "http://localhost/dist/images/paypal.png",
            'class' => "paysafecard",
            'offer' => [
                0 => ["points" => 1500, "price" => 5],
                1 => ["points" => 3000, "price" => 10],
                2 => ["points" => 5400, "price" => 15],
                3 => ["points" => 7800, "price" => 20],
                4 => ["points" => 11400, "price" => 30],
                5 => ["points" => 18300, "price" => 50],
                6 => ["points" => 34200, "price" => 100]
            ]
        ],
        [
            'name' => 'Mobile (Dedipass)',
            'image' => "https://images.badblock.fr/i/dedi.png",
            'class' => "paysafecard",
            'offer' => [
                0 => ["points" => 1500, "price" => 5],
                1 => ["points" => 3000, "price" => 10],
                2 => ["points" => 5400, "price" => 15],
                3 => ["points" => 7800, "price" => 20],
                4 => ["points" => 11400, "price" => 30],
                5 => ["points" => 18300, "price" => 50],
                6 => ["points" => 34200, "price" => 100]
            ]
        ]
    ]
];