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

/**
 *
 * @author Patrik Karlström <patrik@trixon.se>
 */
public class Quake {

    private String mPublicId;
    private Double mMagnitude;
    private String mMagnitudeType;
    private Type mType;
    private TypeCertainty mTypeCertainty;
    private CreationInfo mCreationInfo = new CreationInfo();
    private Origin mOrigin = new Origin();

    public Quake() {
    }

    public CreationInfo getCreationInfo() {
        return mCreationInfo;
    }

    public Double getMagnitude() {
        return mMagnitude;
    }

    public String getMagnitudeType() {
        return mMagnitudeType;
    }

    public Origin getOrigin() {
        return mOrigin;
    }

    public String getPublicId() {
        return mPublicId;
    }

    public Type getType() {
        return mType;
    }

    public TypeCertainty getTypeCertainty() {
        return mTypeCertainty;
    }

    public void setCreationInfo(CreationInfo creationInfo) {
        this.mCreationInfo = creationInfo;
    }

    public void setMagnitude(Double magnitude) {
        this.mMagnitude = magnitude;
    }

    public void setMagnitudeType(String magnitudeType) {
        this.mMagnitudeType = magnitudeType;
    }

    public void setOrigin(Origin origin) {
        this.mOrigin = origin;
    }

    public void setPublicId(String publicId) {
        this.mPublicId = publicId;
    }

    public void setType(Type type) {
        this.mType = type;
    }

    public void setTypeCertainty(TypeCertainty typeCertainty) {
        this.mTypeCertainty = typeCertainty;
    }

    public enum Type {

        NOT_EXISTING("not existing"),
        NOT_REPORTED("not reported"),
        EARTHQUAKE("earthquake"),
        ANTHROPOGENIC_EVENT("anthropogenic event"),
        COLLAPSE("collapse"),
        CAVITY_COLLAPSE("cavity collapse"),
        MINE_COLLAPSE("mine collapse"),
        BUILDING_COLLAPSE("building collapse"),
        EXPLOSION("explosion"),
        ACCIDENTAL_EXPLOSION("accidental explosion"),
        CHEMICAL_EXPLOSION("chemical explosion"),
        CONTROLLED_EXPLOSION("controlled explosion"),
        EXPERIMENTAL_EXPLOSION("experimental explosion"),
        INDUSTRIAL_EXPLOSION("industrial explosion"),
        MINING_EXPLOSION("mining explosion"),
        QUARRY_BLAST("quarry blast"),
        ROAD_CUT("road cut"),
        BLASTING_LEVEE("blasting levee"),
        NUCLEAR_EXPLOSION("nuclear explosion"),
        INDUCED_OR_TRIGGERED_EVENT("induced or triggered event"),
        ROCK_BURST("rock burst"),
        RESERVOIR_LOADING("reservoir loading"),
        FLUID_INJECTION("fluid injection"),
        FLUID_EXTRACTION("fluid extraction"),
        CRASH("crash"),
        PLANE_CRASH("plane crash"),
        TRAIN_CRASH("train crash"),
        BOAT_CRASH("boat crash"),
        OTHER_EVENT("other event"),
        ATMOSPHERIC_EVENT("atmospheric event"),
        SONIC_BOOM("sonic boom"),
        SONIC_BLAST("sonic blast"),
        ACOUSTIC_NOISE("acoustic noise"),
        THUNDER("thunder"),
        AVALANCHE("avalanche"),
        SNOW_AVALANCHE("snow avalanche"),
        DEBRIS_AVALANCHE("debris avalanche"),
        HYDROACOUSTIC_EVENT("hydroacoustic event"),
        ICE_QUAKE("ice quake"),
        SLIDE("slide"),
        LANDSLIDE("landslide"),
        ROCKSLIDE("rockslide"),
        METEORITE("meteorite"),
        VOLCANIC_ERUPTION("volcanic eruption");
        private final String mValue;

        private Type(String v) {
            mValue = v;
        }

        public String value() {
            return mValue;
        }

        public static Type fromValue(String v) {
            for (Type c : Type.values()) {
                if (c.mValue.equals(v)) {
                    return c;
                }
            }
            throw new IllegalArgumentException(v);
        }
    }

    public enum TypeCertainty {

        KNOWN("known"),
        SUSPECTED("suspected");
        private final String mValue;

        private TypeCertainty(String v) {
            mValue = v;
        }

        public String value() {
            return mValue;
        }

        public static TypeCertainty fromValue(String v) {
            for (TypeCertainty c : TypeCertainty.values()) {
                if (c.mValue.equals(v)) {
                    return c;
                }
            }
            throw new IllegalArgumentException(v);
        }

    }

}
