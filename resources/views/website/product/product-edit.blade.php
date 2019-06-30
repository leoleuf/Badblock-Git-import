@extends('layouts.app')

@section('header')

    <!-- Include Editor style. -->
    <link href="https://cdn.jsdelivr.net/npm/froala-editor@2.9.0/css/froala_editor.pkgd.min.css" rel="stylesheet"
          type="text/css"/>
    <link href="https://cdn.jsdelivr.net/npm/froala-editor@2.9.0/css/froala_style.min.css" rel="stylesheet"
          type="text/css"/>

@endsection

@section('content')
    <div class="content-page">
        <!-- Start content -->
        <div class="content">
            <div class="container">
                <div class="row">
                    <div class="col-12">
                        <div class="panel">
                            <div class="panel-body">
                                <div class="card-box">
                                    <!-- will be used to show any messages -->
                                    @if (Session::has('message'))
                                        <div class="alert alert-info">{{ Session::get('message') }}</div>
                                    @endif
                                    <div class="panel">
                                        <div class="panel-body">
                                            <h1>Edition de {{ $Product['name'] }}</h1>
                                            <div class="">
                                                {{ Form::model($Product, array('url' => "/website/crud/product/" . $Product['id'], 'method' => 'PUT','class'=>'form_inline')) }}
                                                <div class="row">
                                                    <div class="col-12 col-md-6">
                                                        <div class="form-group">
                                                            <label>Nom d'affichage du produit :</label>
                                                            {{ Form::text('name', $Product['name'], array('class' => 'form-control','placeholder'=>"Nom d'affichage du produit :")) }}
                                                        </div>
                                                    </div>
                                                    <div class="col-12 col-md-6">
                                                        <div class="form-group">
                                                            <label>Prix :</label>
                                                            {{ Form::text('price', $Product['price'], array('class' => 'form-control','placeholder'=>"Prix du produit")) }}
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-12 col-md-6">
                                                        <div class="form-group">
                                                            <label>Type de livraison :</label>
                                                            {{ Form::text('mode', $Product['mode'],array('class' => 'form-control','placeholder'=>"Type du produit")) }}
                                                        </div>
                                                    </div>
                                                    <div class="col-12 col-md-6">
                                                        <div class="form-group">
                                                            <label>Sélectionner la catégorie :</label>
                                                            <select name="category" class="form-control select2">
                                                                @foreach($cat as $key => $value)
                                                                    @if($Product['cat_id'] == $value->_id)
                                                                        <option value="{{ $value->_id }}"
                                                                                selected>{{ $value->name }}</option>
                                                                    @else
                                                                        <option value="{{ $value->_id }}">{{ $value->name }}</option>
                                                                    @endif
                                                                @endforeach
                                                            </select>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-12 col-md-6">
                                                        <div class="form-group">
                                                            <label>Nom de la Queue (si ShopLinker) :</label>
                                                            {{ Form::text('queue',$Product['queue'],array('class' => 'form-control','placeholder'=>"Ex : hub,skyb")) }}
                                                        </div>
                                                    </div>
                                                    <div class="col-12 col-md-6">
                                                        <div class="form-group">
                                                            <label>Commande (use %player%) :</label>
                                                            {{ Form::text('command',$Product['command'],array('class' => 'form-control','placeholder'=>"Ex : give %player% 137")) }}
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-12 col-md-6">
                                                        <div class="form-group">
                                                            <label>Activer le mode promotion sur ce produit :</label>
                                                            {{ Form::checkbox('promotion', null, $Product['promotion'], array('data-plugin' => 'switchery', 'data-color' => '#00b19d')) }}
                                                        </div>
                                                    </div>
                                                    <div class="col-12 col-md-6">
                                                        <div class="form-group">
                                                            <label>Pourcentage de promotion :</label>
                                                            {{ Form::text('promo_coef', $Product['promo_coef'], array('class' => 'form-control','placeholder'=>"50%")) }}
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-12 col-md-6">
                                                        <div class="form-group">
                                                            <label>Nouveau prix de promotion :</label>
                                                            {{ Form::text('promotion_new_price', $Product['promotion_new_price'], array('class' => 'form-control','placeholder'=>"50%")) }}
                                                        </div>
                                                    </div>
                                                    <div class="col-12 col-md-6">
                                                        <div class="form-group">
                                                            <label>Visibilité dans le container promotion :</label>
                                                            {{ Form::checkbox('promotion_view', null, $Product['promotion_view'], array('data-plugin' => 'switchery', 'data-color' => '#00b19d')) }}
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-12 col-md-6">
                                                        <div class="form-group">
                                                            <label>Nom de la dépendance (si nécessaire) :</label>
                                                            {{ Form::text('depend_name',"",array('class' => 'form-control','placeholder'=>"Ex : gold")) }}
                                                        </div>
                                                    </div>
                                                    <div class="col-12 col-md-6">
                                                        <div class="form-group">
                                                            <label>Activer le mode de dépendance sur ce produit
                                                                :</label>
                                                            {{ Form::checkbox('depend', null, $Product['depend'], array('data-plugin' => 'switchery', 'data-color' => '#00b19d')) }}
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-12 col-md-6">
                                                        <div class="form-group">
                                                            <label>Id de dépendance :</label>
                                                            {{ Form::text('depend_to',$Product['depend_to'],array('class' => 'form-control','placeholder'=>"Nom du group de dépendance")) }}
                                                        </div>
                                                    </div>
                                                    <div class="col-12 col-md-6">
                                                        <div class="form-group">
                                                            <label>Nom unique de dépendance :</label>
                                                            {{ Form::text('depend_name',$Product['depend_name'],array('class' => 'form-control','placeholder'=>"Nom du group de dépendance")) }}
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-12 col-md-6">
                                                        <div class="form-group">
                                                            <label>Activer la visibilité du produit sur le site
                                                                :</label>
                                                            {{ Form::checkbox('visibility', null, $Product['visibility'], array('data-plugin' => 'switchery', 'data-color' => '#00b19d')) }}
                                                        </div>
                                                    </div>
                                                    <div class="col-12 col-md-6">
                                                        <div class="form-group">
                                                            <label>Image du produit :</label>
                                                            {{ Form::text('image', $Product['image'], array('class' => 'form-control','placeholder'=>"URL vers l'image")) }}
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-12">
                                                        <div class="form-group">
                                                            <label>Description du produit (HTML) :</label>
                                                            <textarea class="form-control" name="desc" rows="10"
                                                                      cols="50">{{ $Product['description'] }}</textarea>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-12 col-md-6">
                                                        <div class="form-group">
                                                            <label>Item pour boutique Ingame :</label>
                                                            {{ Form::text('ig_item', $Product['ig_material'], array('class' => 'form-control','placeholder'=>"")) }}
                                                        </div>
                                                    </div>
                                                    <div class="col-12 col-md-6">
                                                        <div class="form-group">
                                                            <label>Ingame data :</label>
                                                            {{ Form::text('ig_data', $Product['ig_data'], array('class' => 'form-control','placeholder'=>"")) }}
                                                        </div>
                                                    </div>
                                                </div>


                                                <button type="submit" class="btn btn-success"><span
                                                            class="glyphicon glyphicon-saved"></span> Save
                                                </button>
                                                {{ Form::close() }}

                                                {{ Form::open(array('url' => 'website/crud/Product/' . $Product['id'])) }}
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
    </div>


@endsection

@section('after_scripts')

    <script type="text/javascript"
            src="https://cdn.jsdelivr.net/npm/froala-editor@2.9.0/js/froala_editor.pkgd.min.js"></script>

    <script type="text/javascript">
        $(function () {
            $('textarea[name= desc]').froalaEditor({
                heightMin: 300,
                heightMax: 500
            });
        });
        $('.form_inline').submit((e) => {
            alert($('#debug_check').value());
            e.preventDefault();
        });
    </script>

@endsection

