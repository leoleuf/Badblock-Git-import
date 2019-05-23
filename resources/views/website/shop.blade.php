@extends('layouts.app')
@section('content')
    <div class="content-page">
        <div class="content">
            <div class="container">
                <div class="row">
                    <div class="col-12">
                        <nav>
                            <div class="nav nav-tabs nav-fill" id="nav-tab" role="tablist">
                                @foreach($servers as $server)
                                    <a class="nav-item nav-link @if($loop->first) active @endif" data-toggle="tab" href="#{{ $server->_id }}" role="tab">{{ $server->name }}</a>
                                @endforeach
                                <a class="btn btn-outline-danger nav-item" href="/website/crud/server/create"><i class="far fa-plus-square"></i> Ajouter un serveur</a>
                            </div>
                        </nav>
                        <div class="tab-content" id="nav-tabContent">
                            @foreach($servers as $server)
                                <div class="tab-pane fade @if($loop->first) show active @endif" id="{{ $server->_id }}" role="tabpanel">
                                    <div class="row">
                                        <div class="col-2">
                                            <div class="nav flex-column nav-pills" id="v-pills-tab" role="tablist">
                                                <a class="btn btn-primary btn-block" href="/website/crud/server/{{ $server->_id }}/edit">Éditer le serveur</a>
                                                @foreach($cat as $category)
                                                    @if($category->server_id == $server->_id)
                                                        <a class="btn btn-dark btn-block @if($loop->first) active @endif" data-toggle="pill" href="#cat-{{ $category->_id }}" role="tab">{{ $category->name }}</a>
                                                    @endif
                                                @endforeach
                                                <a class="btn btn-outline-danger btn-block" href="/website/crud/category/create">Ajouter une catégorie</a>
                                                <button type="button" class="btn btn-danger" data-toggle="modal" data-target="#deleteModal" onclick="changeModalValues('server', '{{ $server->id }}', '{{ $server->name }}')">Supprimer le serveur</button>
                                            </div>
                                        </div>
                                        <div class="col-10">
                                            <div class="tab-content" id="v-pills-tabContent">
                                                @foreach($cat as $category)
                                                    @if($category->server_id == $server->_id)
                                                        <div class="tab-pane fade @if($loop->first) show active @endif" id="cat-{{ $category->_id }}" role="tabpanel">
                                                            <div class="btn-group float-right">
                                                                <a class="btn btn-outline-danger" href="/website/crud/product/create"><i class="far fa-plus-square"></i> Créer un produit</a>
                                                                <a class="btn btn-primary" href="/website/crud/category/{{ $category->_id }}/edit">Éditer la catégorie</a>
                                                                <button type="button" class="btn btn-danger" data-toggle="modal" data-target="#deleteModal" onclick="changeModalValues('category', '{{ $category->id }}', '{{ $category->name }}')">Supprimer la catégorie</button>
                                                            </div>
                                                            <table class="table table-hover">
                                                                <thead>
                                                                <tr>
                                                                    <th>Produit</th>
                                                                    <th>Prix</th>
                                                                    <th>Dépendance</th>
                                                                    <th>Achat unique</th>
                                                                    <th>Action</th>
                                                                </tr>
                                                                </thead>
                                                                <tbody>
                                                                @foreach($products as $product)
                                                                    @if($product->cat_id == $category->_id)
                                                                        <tr>
                                                                            <td>{{ $product->name }}</td>
                                                                            <td>{{ $product->price }}</td>
                                                                            <td>@if($product->depend)Oui @else Non @endif</td>
                                                                            <td>@if($product->buy_one)Oui @else Non @endif</td>
                                                                            <td>
                                                                                <a href="/website/crud/product/{{ $product->id }}/edit" class="btn btn-primary">Édition</a>
                                                                                <button type="button" class="btn btn-danger" data-toggle="modal" data-target="#deleteModal" onclick="changeModalValues('product', '{{ $product->id }}', '{{ $product->name }}')">Supprimer</button>
                                                                            </td>
                                                                        </tr>
                                                                    @endif
                                                                @endforeach
                                                                </tbody>
                                                            </table>
                                                        </div>
                                                    @endif
                                                @endforeach
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            @endforeach

                                <div class="modal fade" id="deleteModal" tabindex="-1" role="dialog" aria-labelledby="DeleteModal" aria-hidden="true">
                                    <div class="modal-dialog" role="document">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <h5 class="modal-title">Confirmation de suppression</h5>
                                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                                    <span aria-hidden="true">&times;</span>
                                                </button>
                                            </div>
                                            <div class="modal-body" id="deleteModal_body">
                                            </div>
                                            <div class="modal-footer">
                                                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>

                                                {{ Form::open(array('id' => 'deleteForm', 'url' => 'Default value, will generate an error')) }}
                                                {{ Form::hidden('_method', 'DELETE') }}
                                                {{ Form::submit('Delete', array('class' => 'btn btn-danger')) }}
                                                {{ Form::close() }}
                                            </div>
                                        </div>
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
    <script>

        function changeModalValues(type, id, name){
            var modalBody = $("#deleteModal_body");
            var deleteForm = $("#deleteForm");
            var typeFrench = "default value";

            switch(type){
                case 'server':
                    typeFrench = "le serveur";
                    break;
                case 'category':
                    typeFrench = "la catégorie";
                    break;
                case 'product':
                    typeFrench = "le produit";
                    break;
            }

            modalBody.html("Êtes-vous sûr de vouloir supprimer "+typeFrench+" <b>"+name+"</b> ? (Toute suppression est définitive)");
            deleteForm.attr('action', "/website/crud/"+type+"/"+id);
        }

    </script>
@endsection
