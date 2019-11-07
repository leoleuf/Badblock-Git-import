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
                                Merci de notifier les personnes ayant oublié.<br />
                                Total de preuves oubliées ces deux dernières semaines : {{ count($Sanctions) }} <br />
                                @can('proof_top')
                                    Top des modérateurs sans preuves : <a href="/section/preuves/top">Accéder au top</a>
                                @endcan
                            </p>
                            <table class="table" id="datatable-editable">
                                <thead>
                                <tr>
                                    <th>Banner</th>
                                    <th>Action</th>
                                    <th>Pseudo</th>
                                    <th>Raison</th>
                                    <th>Date</th>
                                    <th>Temps</th>
                                    <th>Action</th>
                                </tr>
                                </thead>
                                <tbody>
                                @foreach($Sanctions as $k => $row)
                                    <tr class="gradeX">
                                        <td>{{ ucfirst($row['punisher']) }}</td>
                                        <td>{{ $row['type'] }}</td>
                                        <td>{{$row['pseudo'] }}</td>
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
                                            <button class="btn btn-success" id="check_sanction" onclick="sanction_checked('{{ $k }}')"><i class="fas fa-check"></i></button>
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

        function sanction_checked(k) {

            $.ajax({
                url: '/section/preuves/checked',
                method: 'post',
                data: { id: k }
            }).fail(function(){
                toastr.error('Ooops...', "Une erreur est survenu");
            }).always(function(){
                toastr.warning('Patientez...', "Requête en cours");
            }).done(function(){
                toastr.success('Terminer', "L'action à bien été effectuée");
                location.reload();
            });

        }

    </script>


@endsection