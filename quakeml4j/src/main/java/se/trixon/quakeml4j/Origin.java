/*
 * Copyright 2026 Patrik Karlström <patrik@trixon.se>.
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
package se.trixon.quakeml4j;

import java.time.OffsetDateTime;

/**
 *
 * @author Patrik Karlström <patrik@trixon.se>
 */
public class Origin {

    private Double mDepth;
    private DepthType mDepthType;
    private Double mLatitude;
    private Double mLongitude;
    private OffsetDateTime mTime;
    private Type mType;

    public Origin() {
    }

    public Double getDepth() {
        return mDepth;
    }

    public DepthType getDepthType() {
        return mDepthType;
    }

    public Double getLatitude() {
        return mLatitude;
    }

    public Double getLongitude() {
        return mLongitude;
    }

    public OffsetDateTime getTime() {
        return mTime;
    }

    public Type getType() {
        return mType;
    }

    public void setDepth(Double depth) {
        mDepth = depth;
    }

    public void setDepthType(DepthType depthType) {
        mDepthType = depthType;
    }

    public void setLatitude(Double latitude) {
        mLatitude = latitude;
    }

    public void setLongitude(Double longitude) {
        mLongitude = longitude;
    }

    public void setTime(OffsetDateTime time) {
        mTime = time;
    }

    public void setType(Type type) {
        mType = type;
    }

    public enum DepthType {

        FROM_LOCATION("from location"),
        FROM_MOMENT_TENSOR_INVERSION("from moment tensor inversion"),
        FROM_MODELING_OF_BROAD_BAND_P_WAVEFORMS("from modeling of broad-band P waveforms"),
        CONSTRAINED_BY_DEPTH_PHASES("constrained by depth phases"),
        CONSTRAINED_BY_DIRECT_PHASES("constrained by direct phases"),
        CONSTRAINED_BY_DEPTH_AND_DIRECT_PHASES("constrained by depth and direct phases"),
        OPERATOR_ASSIGNED("operator assigned"),
        OTHER("other");
        private final String mValue;

        private DepthType(String v) {
            mValue = v;
        }

        public String value() {
            return mValue;
        }

        public static DepthType fromValue(String v) {
            for (DepthType c : DepthType.values()) {
                if (c.mValue.equals(v)) {
                    return c;
                }
            }
            return null;
            //throw new IllegalArgumentException(v);
        }

    }

    public enum Type {

        HYPOCENTER("hypocenter"),
        CENTROID("centroid"),
        AMPLITUDE("amplitude"),
        MACROSEISMIC("macroseismic"),
        RUPTURE_START("rupture start"),
        RUPTURE_END("rupture end");
        private final String mValue;

        public static Type fromValue(String v) {
            for (Type c : Type.values()) {
                if (c.mValue.equals(v)) {
                    return c;
                }
            }
            throw new IllegalArgumentException(v);
        }

        private Type(String v) {
            mValue = v;
        }

        public String value() {
            return mValue;
        }

    }

}
