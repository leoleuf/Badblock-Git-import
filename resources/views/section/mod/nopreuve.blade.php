@extends('layouts.app')
@section('content')
    <div class="container">
        <div class="row">
            <div class="col-12">
                <div class="panel">
                    <div class="panel-body">
                        <div class="card-box">
                            <h4 class="m-t-0 header-title">Sanctions sans preuves.</h4>
                            <p class="text-muted font-14 m-b-20">
                                Merci de notifier les personnes ayant oublié.
                            </p>
                            <table class="table" id="datatable-editable">
                                <thead>
                                <tr>
                                    <th>Banner</th>
                                    <th>Action</th>
                                    <th>Raison</th>
                                    <th>Date</th>
                                    <th>Temps</th>
                                    <th>#</th>
                                </tr>
                                </thead>
                                <tbody>
                                @foreach($Sanctions as $k => $row)
                                    <tr class="gradeX">
                                        <td>{{ ucfirst($row['punisher']) }}</td>
                                        <td>{{ $row['type'] }}</td>
                                        <td>{{ $row['reason'] }}</td>
                                        <td>{{ App\Http\Controllers\DateController::formatDateString($row['date']) }}</td>
                                        <td>{{ date('d/m/Y à H:m', round(($row['expire'] / 1000), 0)) }}</td>
                                        <td>
                                            <form id="form{{ $k }}">
                                                <input name="banner" value="{{ $row['punisher'] }}"
                                                       type="hidden">
                                                <input type="button" id="notif" value="Notifier"
                                                       onclick="notiff('{{ $k }}');" class="btn btn-info"/>
                                            </form>
                                        </td>
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
    </div>
@endsection
@section("after_scripts")
    <script src="/assets/plugins/toastr/toastr.min.js"></script>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.form/4.2.2/jquery.form.min.js"
            integrity="sha384-FzT3vTVGXqf7wRfy8k4BiyzvbNfeYjK+frTVqZeNDFl8woCbF0CYG6g2fMEFFo/i"
            crossorigin="anonymous"></script>

    <script>

        function notiff(k) {
            toastr.success('Preuve(s) ajoutée(s)', "Utilisateur notifié !");
            $("#form" + k).ajaxSubmit({url: '/section/preuves', type: 'post'});
        }

    </script>


@endsection