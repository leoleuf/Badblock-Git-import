@extends('layouts.app')
@section('content')
    <div class="content-page">
        <!-- Start content -->
        <div class="content">
            <div class="container">
                <div class="row"><!-- end col -->
                    <div class="col-lg-4 col-md-6">
                        <div class="card-box" style="height: 220px;">
                            <h4 class="header-title m-t-0 m-b-30">Décaissement Pts / Jour</h4>

                            <div class="widget-box-2">
                                <div class="widget-detail-2">
                                    @if($data["dec_d"] > $data["dec_d_last"])
                                        <span class="badge badge-success pull-left m-t-20">{{ round(( $data["dec_d"] - $data["dec_d_last"]) / $data["dec_d"] * 100, 2) }}% <i
                                                    class="zmdi zmdi-trending-up"></i> </span>
                                    @elseif($data["dec_d"] == $data["dec_d_last"])
                                        <span class="badge badge-warning pull-left m-t-20">0% <i
                                                    class="zmdi zmdi-trending-up"></i> </span>
                                    @else
                                        <span class="badge badge-danger pull-left m-t-20">-{{ round(($data["dec_d_last"] - $data["dec_d"]) / $data["dec_d"] * 100,2) }}% <i
                                                    class="zmdi zmdi-trending-down"></i> </span>
                                    @endif
                                    <h2 class="m-b-0"> {{ $data["dec_d"] }}</h2>
                                    <p class="text-muted m-b-25">(Last : {{ $data["dec_d_last"] }})</p>
                                </div>
                                <div class="progress progress-bar-success-alt progress-sm m-b-0">
                                    <div class="progress-bar progress-bar-success" role="progressbar"
                                         aria-valuenow="77" aria-valuemin="0" aria-valuemax="100"
                                         @if($data["dec_d"] > $data["dec_d_last"])
                                         style="width: 100%;">
                                        @elseif($data["dec_d"] < $data["dec_d_last"])
                                            style="width: 0%;">
                                        @else
                                            style="width: {{ $data["dec_d"] / $data["dec_d_last"] * 100 }}%;">
                                        @endif
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div><!-- end col -->
                    <div class="col-lg-4 col-md-6">
                        <div class="card-box" style="height: 220px;">
                            <div class="widget-box-2">
                                <h4 class="header-title m-t-0 m-b-30">Décaissement Pts / Mois</h4>
                                <div class="widget-detail-2">
                                    @if($data["dec_m"] > $data["dec_m_last"])
                                        <span class="badge badge-success pull-left m-t-20">{{ round(( $data["dec_m"] - $data["dec_m_last"]) / $data["dec_m"] * 100, 2) }}% <i
                                                    class="zmdi zmdi-trending-up"></i> </span>
                                    @elseif($data["dec_m"] == $data["dec_m_last"])
                                        <span class="badge badge-warning pull-left m-t-20">0% <i
                                                    class="zmdi zmdi-trending-up"></i> </span>
                                    @else
                                        <span class="badge badge-danger pull-left m-t-20">-{{ round(($data["dec_m_last"] - $data["dec_m"]) / $data["dec_m"] * 100,2) }}% <i
                                                    class="zmdi zmdi-trending-down"></i> </span>
                                    @endif
                                    <h2 class="m-b-0"> {{ $data["dec_m"] }}</h2>
                                    <p class="text-muted m-b-25">(Last : {{ $data["dec_m_last"] }})</p>
                                </div>
                                <div class="progress progress-bar-success-alt progress-sm m-b-0">
                                    <div class="progress-bar progress-bar-success" role="progressbar"
                                         aria-valuenow="77" aria-valuemin="0" aria-valuemax="100"
                                         @if($data["dec_d"] > $data["dec_m_last"])
                                         style="width: 100%;"
                                         @elseif($data["dec_d"] < $data["dec_m_last"])
                                         style="width: 0%;">
                                        @else
                                            style="width: {{ $data["dec_d"] / $data["dec_m_last"] * 100 }}%;">
                                        @endif
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div><!-- end col -->
                    <div class="col-lg-12">
                        <div class="card-box">
                            <h4 class="header-title m-t-0">Décaissement sur le mois en cours</h4>
                            <div id="dec" style="min-width: 310px; height: 300px; margin: 0 auto"></div>
                        </div>
                    </div><!-- end col -->
                </div>
            </div>
        </div>
        <script
                src="https://code.jquery.com/jquery-3.3.1.min.js"
                integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8="
                crossorigin="anonymous"></script>
        <script src="/assets/plugins/morris/morris.min.js"></script>
        <script src="/assets/plugins/raphael/raphael-min.js"></script>


        <script>

            !function ($) {
                "use strict";

                var MorrisCharts = function () {
                };

                //creates line chart
                MorrisCharts.prototype.createLineChart = function (element, data, xkey, ykeys, labels, opacity, Pfillcolor, Pstockcolor, lineColors) {
                    Morris.Line({
                        element: element,
                        data: data,
                        xkey: xkey,
                        ykeys: ykeys,
                        labels: labels,
                        fillOpacity: opacity,
                        pointFillColors: Pfillcolor,
                        pointStrokeColors: Pstockcolor,
                        behaveLikeLine: true,
                        gridLineColor: '#2f3e47',
                        gridTextColor: '#98a6ad',
                        hideHover: 'auto',
                        lineWidth: '3px',
                        pointSize: 0,
                        preUnits: '€',
                        resize: true, //defaulted to true
                        lineColors: lineColors
                    });
                },

                    MorrisCharts.prototype.init = function () {

                        var $data = [
                                @foreach($data['op_chart'] as $row)
                            {
                                y: '{{ $row['date']}}', a: {{ $row['result'] }}, b: {{ $row['result_last'] }} },
                            @endforeach
                        ];
                        this.createLineChart('dec', $data, 'y', ['a', 'b'], ['Mois en cours', 'Mois dernier'], ['0.1'], ['#ffffff'], ['#999999'], ['#ff8acc', '#5b69bc']);

                    },
                    //init
                    $.MorrisCharts = new MorrisCharts, $.MorrisCharts.Constructor = MorrisCharts
            }(window.jQuery),

                //initializing
                function ($) {
                    "use strict";
                    $.MorrisCharts.init();
                }(window.jQuery);

        </script>

@endsection