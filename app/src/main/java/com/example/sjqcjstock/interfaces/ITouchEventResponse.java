/*
 * ITouchEventResponse.java
 * Android-Charts
 *
 * Created by limc on 2011/05/29.
 *
 * Copyright 2011 limc.cn All rights reserved.
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

package com.example.sjqcjstock.interfaces;


import com.example.sjqcjstock.view.stocks.GridChart;

/**
 * <p>支持响应事件消息的此类对象接口</p>
 *
 * @author limc
 * @version v1.0 2013/05/30 17:57:32
 * @see ITouchEventNotify
 */
public interface ITouchEventResponse {

    /**
     * <p>响应通知</p>
     *
     * @param chart <p>源头对象</p>
     */
    public void notifyEvent(GridChart chart);
}
