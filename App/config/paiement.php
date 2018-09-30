<?php
return [
    'paiement' => [
        [
            'name' => 'Carte Bancaire & PayPal',
            'image' => "https://cdn.badblock.fr/wd/i/paypal.png",
            'class' => "paypal",
            //EX 5.0
            'offer' => [
                0 => ["points" => 1500*1.25, "price" => 5.0],
                1 => ["points" => 3000*1.25, "price" => 10.0],
                2 => ["points" => 5400*1.25, "price" => 15.0],
                3 => ["points" => 7800*1.25, "price" => 20.0],
                4 => ["points" => 11400*1.25, "price" => 30.0],
                5 => ["points" => 18300*1.25, "price" => 50.0],
                6 => ["points" => 34200*1.25, "price" => 100.0],
                7 => ["points" => 85000*1.25, "price" => 250.0]
            ]
        ],
        [
            'name' => 'PaySafeCard',
            'image' => "https://cdn.badblock.fr/wd/i/paysafecard.png",
            'class' => "paysafecard",
            'offer' => [
                0 => ["points" => 1500*1.25, "price" => 5, "document_id" => 415142, "private_id" => 233211],
                1 => ["points" => 3000*1.25, "price" => 10, "document_id" => 415139, "private_id" => 233211],
                2 => ["points" => 4500*1.25, "price" => 15, "document_id" => 423728, "private_id" => 233211],
                3 => ["points" => 6000*1.25, "price" => 20, "document_id" => 415143, "private_id" => 233211],
                4 => ["points" => 15000*1.25, "price" => 50, "document_id" => 415145, "private_id" => 233211]
            ]
        ],
        [
            'name' => 'Mobile (Dedipass)',
            'image' => "https://images.badblock.fr/i/dedi.png",
            'class' => "paysafecard",
            'offer' => [
                0 => ["points" => 1500*1.25, "price" => 5],
                1 => ["points" => 3000*1.25, "price" => 10],
                2 => ["points" => 5400*1.25, "price" => 15],
                3 => ["points" => 7800*1.25, "price" => 20],
                4 => ["points" => 11400*1.25, "price" => 30],
                5 => ["points" => 18300*1.25, "price" => 50],
                6 => ["points" => 34200*1.25, "price" => 100]
            ]
        ]
    ]
];