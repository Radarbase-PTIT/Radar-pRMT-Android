/*
 * Copyright 2017 The Hyve
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.radarcns.detail

import android.Manifest.permission.RECEIVE_BOOT_COMPLETED
import android.Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
import android.Manifest.permission.SYSTEM_ALERT_WINDOW
import org.radarbase.android.RadarConfiguration.Companion.START_AT_BOOT
import org.radarbase.android.RadarService
import org.radarbase.android.config.SingleRadarConfiguration
import org.radarbase.android.source.SourceProvider
import org.radarbase.monitor.application.ApplicationStatusProvider
import org.radarbase.passive.audio.OpenSmileAudioProvider
import org.radarbase.passive.bittium.FarosProvider
//import org.radarbase.passive.empatica.E4Provider
import org.radarbase.passive.google.activity.GoogleActivityProvider
import org.radarbase.passive.google.places.GooglePlacesProvider
import org.radarbase.passive.google.sleep.GoogleSleepProvider
import org.radarbase.passive.phone.PhoneBluetoothProvider
import org.radarbase.passive.phone.PhoneContactListProvider
import org.radarbase.passive.phone.PhoneLocationProvider
import org.radarbase.passive.phone.PhoneSensorProvider
import org.radarbase.passive.phone.audio.input.PhoneAudioInputProvider
//import org.radarbase.passive.polar.PolarProvider
import org.radarbase.passive.phone.usage.PhoneUsageProvider
import org.radarbase.passive.weather.WeatherApiProvider

class RadarServiceImpl : RadarService() {
    override val plugins: List<SourceProvider<*>> = listOf(
        ApplicationStatusProvider(this),
        OpenSmileAudioProvider(this),
        // E4Provider(this),
        FarosProvider(this),
//        PolarProvider(this),
        PhoneBluetoothProvider(this),
        PhoneContactListProvider(this),
        PhoneLocationProvider(this),
        PhoneSensorProvider(this),
        PhoneUsageProvider(this),
        WeatherApiProvider(this),
        GoogleActivityProvider(this),
        GoogleSleepProvider(this),
        GooglePlacesProvider(this),
        PhoneAudioInputProvider(this)
    )

    override val servicePermissions: List<String>
        get() = buildList {
            addAll(super.servicePermissions)
            add(RECEIVE_BOOT_COMPLETED)
            add(REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
            if (configuration.latestConfig.getBoolean(START_AT_BOOT, false)) {
                add(SYSTEM_ALERT_WINDOW)
            }
        }

    override fun doConfigure(config: SingleRadarConfiguration) {
        super.doConfigure(config)
        configureRunAtBoot(config, MainActivityBootStarter::class.java)
    }
}
