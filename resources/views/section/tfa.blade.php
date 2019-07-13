@extends('layouts.app')
@section('header')
    <link rel="stylesheet" href="/assets/plugins/magnific-popup/dist/magnific-popup.css"/>
    <link href="/assets/plugins/toastr/toastr.min.css" rel="stylesheet" type="text/css" />
@endsection
@section('content')
    <div class="content-page">
        <!-- Start content -->
        <div class="content">
            <div class="container">
                <h1>Vérification des TFA</h1>
                <div class="row">
                    <div class="col-12">
                        <div class="panel">
                            <div class="panel-body">
                                <div class="card-box">
                                    <table class="table table-striped" id="datatable-editable">
                                        <thead>
                                        <tr>
                                            <th>Pseudo</th>
                                            <th>TFA Active</th>
                                            <th>Bypass</th>
                                            <th>Action</th>
                                            @if(\Illuminate\Support\Facades\Auth::user()->id == 3)
                                            <th>ByPass</th>
                                            @endif
                                        </tr>
                                        </thead>
                                        <tbody>
                                        @foreach($user as $data)
                                            <tr class="gradeX">
                                                <td>{{ App\Http\Controllers\section\TfaController::getUserById($data->id) }}</td>
                                                <td>
                                                    @if(App\Http\Controllers\section\TfaController::checkActiveTFA($data->id))
                                                        Non
                                                    @else
                                                        Oui
                                                    @endif
                                                </td>
                                                <td>
                                                    @if($data->TFAbypass)
                                                        Oui
                                                    @else
                                                        Non
                                                    @endif
                                                </td>
                                                <td>
                                                    <form method="post" action="/section/tfacheck/reset">
                                                        {{ csrf_field() }}
                                                        <input type="hidden" name="user_id" id="userId" value="{{ $data->id }}">
                                                        <button class="btn btn-icon btn-success">
                                                            <i class="fas fa-sync"></i>
                                                        </button>
                                                    </form>
                                                </td>
                                                @if(\Illuminate\Support\Facades\Auth::user()->id == 3)
                                                    <td>
                                                        <form class="inline" method="post" action="/tfacheck/bypass">
                                                            <div class="custom-control custom-checkbox mr-sm-2">
                                                                <input type="checkbox" class="custom-control-input" id="bypassCheck{{ $data->id }}" @if($data->TFAbypass) checked @endif onclick="byPass({{ $data->id}}, {{ $data->TFAbypass }})">
                                                                <label class="custom-control-label" for="bypassCheck{{ $data->id }}"></label>
                                                            </div>
                                                        </form>
                                                    </td>
                                                @endif
                                            </tr>
                                        @endforeach
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                            <!-- end: panel body -->

                        </div> <!-- end panel -->
                    </div> <!-- end col-->
                </div>
                <!-- end row -->

            </div>
        </div>
    </div>
@endsection
@section("after_scripts")
    <script src="/assets/plugins/toastr/toastr.min.js"></script>
    <script>

        function byPass(id, val) {

            if(val == 0)
            {
                val = 1;
            }
            else
            {
                val = 0;
            }

            $.ajax({

                method: 'POST',
                url: '/section/tfacheck/bypass',
                data: {
                    'userid': id,
                    'bypass': + val,
                },
                success: function (data) {
                    toastr.success('Modification du Bypass', "Succès !");
                }

            });

        }

    </script>

@endsection