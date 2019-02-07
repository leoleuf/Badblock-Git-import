@extends('layouts.app')

@section('header')
    <link rel="stylesheet" href="/assets/plugins/magnific-popup/dist/magnific-popup.css"/>
@endsection
@section('content')
    <div class="row">
        <div class="portfolioContainer">
            <div class="row">
                @foreach($Screen as $row)
                    <div class="col-md-4 col-xl-6 col-lg-6 natural personal">
                        <div class="gal-detail thumb">
                            <a href="https://cdn.badblock.fr/upload/{{ $row['file_name'] }}" class="image-popup" title="Screenshot-1">
                                <img src="https://cdn.badblock.fr/upload/{{ $row['file_name'] }}" class="thumb-img" alt="work-thumbnail">
                            </a>
                            <h5>{{ $row['file_name'] }} - {{ $row['date'] }} - {{ $row['ip'] }}</h5>
                        </div>
                    </div>
                @endforeach
            </div>
        </div>
    </div>
    <ul class="pagination pagination-split">
        @for($i = 1; $i <= $Page ; $i++)
            <li class="page-item"><a class="page-link" href="/screen/{{ $i }}">{{ $i }}</a></li>
        @endfor
    </ul>
@endsection
@section('after_scripts')
    <script type="text/javascript" src="/assets/plugins/isotope/dist/isotope.pkgd.min.js"></script>
    <script type="text/javascript" src="/assets/plugins/magnific-popup/dist/jquery.magnific-popup.min.js"></script>
    <script type="text/javascript">
        $(window).on('load', function () {
            var $container = $('.portfolioContainer');
            $container.isotope({
                filter: '*',
                animationOptions: {
                    duration: 750,
                    easing: 'linear',
                    queue: false
                }
            });

            $('.portfolioFilter a').click(function(){
                $('.portfolioFilter .current').removeClass('current');
                $(this).addClass('current');

                var selector = $(this).attr('data-filter');
                $container.isotope({
                    filter: selector,
                    animationOptions: {
                        duration: 750,
                        easing: 'linear',
                        queue: false
                    }
                });
                return false;
            });
        });
        $(document).ready(function() {
            $('.image-popup').magnificPopup({
                type: 'image',
                closeOnContentClick: true,
                mainClass: 'mfp-fade',
                gallery: {
                    enabled: true,
                    navigateByImgClick: true,
                    preload: [0,1] // Will preload 0 - before current, and 1 after the current image
                }
            });
        });
    </script>
@endsection
