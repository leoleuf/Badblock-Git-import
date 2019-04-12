@extends('layouts.app')
@section('content')
    <div class="content-page">
        <div class="content">
            <div class="container">
                <div class="row">
                    <div class="col-12">
                        <div class="panel">
                            <div class="panel-body">
                                <div class="card-box">
                                    <div class="webserver-list server-button">
                                        @foreach($servers as $serv)
                                            <form class="form-inline" method="post" action="/visiblity/{{ $serv->name }}">
                                                <div class="btn-webserver" data-filter=".{{ $serv->_id }}">
                                                    {{ $serv->name }}
                                                    <div class="custom-control custom-checkbox">
                                                        <input type="checkbox" class="custom-control-input" id="customCheck1" @if($serv->visibility) checked @endif>
                                                        <label class="custom-control-label" for="customCheck1"></label>
                                                    </div>
                                                </div>
                                            </form>
                                        @endforeach
                                    </div>
                                </div>
                                <div class="server-filter-content">
                                    @foreach($cat as $cats)
                                        <div class="card-box {{ $cats->server_id }}">
                                            <div class="servercat">
                                                {{ $cats->name }}
                                                <div class="servercat-btn">
                                                    <a href="/website/crud/category/{{ $cats->_id  }}/edit" class="btn btn-success">Edition</a>
                                                    <a href="#" class="btn btn-danger">Supprimer</a>
                                                </div>
                                            </div>

                                            <table class="table">
                                                <thead>
                                                <tr>
                                                    <th>#</th>
                                                    <th>Produit</th>
                                                    <th>Prix</th>
                                                    <th>Dépendance</th>
                                                    <th>Achat unique</th>
                                                    <th>Action</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                @php $i = 0 @endphp
                                                @foreach($products as $product)
                                                    @if($product->cat_id == $cats->_id)
                                                        <tr>
                                                            <th scope="row">{{ $i }}</th>
                                                            <td>{{ $product->name }}</td>
                                                            <td>{{ $product->price }}</td>
                                                            <td>@if($product->depend)Oui @else Non @endif</td>
                                                            <td>@if($product->buy_one)Oui @else Non @endif</td>
                                                            <td><a href="/website/crud/product/{{ $product->id }}/edit" class="btn btn-success">Edition</a></td>
                                                        </tr>
                                                    @endif
                                                    @php $i++ @endphp
                                                @endforeach

                                                </tbody>
                                            </table>
                                        </div>
                                    @endforeach
                                </div>

                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
@endsection
@section("after_scripts")
    <script src="https://unpkg.com/isotope-layout@3/dist/isotope.pkgd.min.js"></script>
    <script>
        function gallery_isotope() {
            if ( $('.server-filter-content').length ){
                $(".server-filter-content").isotope({
                    layoutMode: 'fitRows',
                    animationOptions: {
                        duration: 750,
                        easing: 'linear'
                    }
                });

                // Add isotope click function
                $(".server-button .btn-webserver").on('click', function() {
                    $(".server-button .btn-webserver").removeClass("active");
                    $(this).addClass("active");

                    var selector = $(this).attr("data-filter");
                    $(".server-filter-content").isotope({
                        filter: selector,
                        animationOptions: {
                            duration: 450,
                            easing: "linear",
                            queue: false,
                        }
                    });
                    return false;
                });
            }
        }
        gallery_isotope();
    </script>
@endsection
