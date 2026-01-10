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
public class CreationInfo {

    private String mAgencyId;
    private String mAuthor;
    private OffsetDateTime mCreationTime;

    public CreationInfo() {
    }

    public String getAgencyId() {
        return mAgencyId;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public OffsetDateTime getCreationTime() {
        return mCreationTime;
    }

    public void setAgencyId(String agencyId) {
        mAgencyId = agencyId;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public void setCreationTime(OffsetDateTime creationTime) {
        mCreationTime = creationTime;
    }

}
