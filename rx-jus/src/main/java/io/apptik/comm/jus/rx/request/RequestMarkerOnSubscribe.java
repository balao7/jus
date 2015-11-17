/*
 * Copyright (C) 2015 AppTik Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.apptik.comm.jus.rx.request;

import io.apptik.comm.jus.Listener;
import io.apptik.comm.jus.JusLog;
import io.apptik.comm.jus.Request;
import io.apptik.comm.jus.rx.BaseSubscription;
import io.apptik.comm.jus.rx.event.MarkerEvent;
import rx.Observable;
import rx.Subscriber;

public class RequestMarkerOnSubscribe implements Observable.OnSubscribe<MarkerEvent> {
    private final Request request;

    public RequestMarkerOnSubscribe(Request request) {
        this.request = request;
    }

    @Override
    public void call(final Subscriber<? super MarkerEvent> subscriber) {
        final Listener.MarkerListener listener = new Listener.MarkerListener() {
            @Override
            public void onMarker(JusLog.MarkerLog.Marker marker, Object... args) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(new MarkerEvent(request, marker, args));
                }
            }
        };
        request.addMarkerListener(listener);

        subscriber.add(new BaseSubscription() {
            @Override
            protected void doUnsubscribe() {
                request.removeMarkerListener(listener);
            }
        });
    }
}
