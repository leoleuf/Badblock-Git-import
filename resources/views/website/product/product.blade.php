@extends('layouts.app')
@section('content')
    <br>
    <div class="content-page">
        <!-- Start content -->
        <div class="content">
            <div class="container">
    <div class="row">
        <div class="col-sm-12">
            <!-- will be used to show any messages -->
            @if (Session::has('message'))
                <div class="alert alert-info">{{ Session::get('message') }}</div>
            @endif
            <div class="panel">
                <div class="panel-body">
                    <div class="row">
                        <div class="col-sm-6">
                            <div class="m-b-30">
                                <h1>Liste des produits  </h1>
                                <a href="/website" id="addToTable" class="btn btn-primary waves-effect waves-light">Retour <i class="fa fa-long-arrow-left"></i></a>
                                <a href="/website/crud/product/create" id="addToTable" class="btn btn-primary waves-effect waves-light">Add <i class="fa fa-plus"></i></a>
                            </div>
                        </div>
                    </div>

                    <h1>Choisir la catégorie que vous souhaitez afficher</h1><br />
                    <select id="catToDisplay" class="form-control" onchange="displayProductCat()">
                        @foreach($Categories as $row)
                            <option value="{{ str_replace(" ", "_", $row->name) }}">{{ $row->name }}</option>
                            @endforeach
                    </select>
                    <br />


                    @foreach($Categories as $cat)
                    <div id="cat_{{ str_replace(" ", "_", $cat->name) }}" class="" style="display: none;">
                        <table class="table table-striped" id="datatable-editable">
                            <thead>
                            <tr>
                                <th>Nom</th>
                                <th>Prix</th>
                                <th>Catégorie</th>
                                <th>Dépendance</th>
                                <th>Promo</th>
                                <th>Achat Unique</th>
                                <th>Actions</th>
                            </tr>
                            </thead>
                            <tbody>
                               @foreach($ProductsInCat[$cat->name] as $key => $value)
                                   <tr>
                                        <td>{{ $value->name }}</td>
                                        <td>{{ $value->price }}</td>
                                        <td>{{ str_replace("_", " ", $value->cat) }}</td>
                                       <td>
                                           @if($value->depend)
                                               <span class="label label-success">Activé</span>
                                           @else
                                               <span class="label label-danger">Désactivé</span>
                                           @endif
                                       </td>
                                       <td>
                                           @if($value->promo)
                                               <span class="label label-success">Activé</span>
                                           @else
                                               <span class="label label-danger">Désactivé</span>
                                           @endif
                                       </td>
                                        <td>
                                            @if($value->buy_one == true)
                                                <span class="label label-success">Activé</span>
                                            @else
                                                <span class="label label-danger">Désactivé</span>
                                            @endif
                                        </td>
                                        <td>
                                            <div>
                                                <!-- delete the users (uses the destroy method DESTROY /users/{id} -->
                                                {{ Form::open(array('url' => '/website/crud/product/' . $value->id)) }}
                                                <a class="btn btn-small btn-info" href="{{ URL::to('website/crud/product/' . $value->_id . '/edit') }}">Edit</a>

                                                {{ Form::hidden('_method', 'DELETE') }}
                                                {{ Form::submit('Delete', array('class' => 'btn btn-danger')) }}
                                                {{ Form::close() }}
                                            </div>
                                        </td>
                                    </tr>
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
@endsection

@section('after_scripts')

    <script type="text/javascript">

        function displayProductCat() {

            var categories = [
                @foreach($Categories as $cat)
                '{{ str_replace(" ", "_", $cat->name) }}',
                @endforeach
            ];

            for(var i = 0; i < categories.length; i++){
                $("#cat_"+categories[i]).css("display", "none");
            }

            $("#cat_"+$("#catToDisplay").val()).css("display", "block");
        }
        
    </script>

    @endsection
