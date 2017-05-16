/**
 * Copyright (c) 2013-2020, cpthack 成佩涛 (cpt@jianzhimao.com).
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
package com.cpthack.commons.rdclient.queue;

/**
 * <b>IAtom.java</b></br>
 * 
 * <pre>
 * 具有事务性的队列操作接口类
 * </pre>
 *
 * @author cpthack cpt@jianzhimao.com
 * @date 2017年4月16日 下午1:41:37
 * @since JDK 1.7
 */
public interface IAtom {
	
	/**
	 * 
	 * <b>原子性队列数据获取</b> <br/>
	 * <br/>
	 * 
	 * 与队列取数据时保持事务性操作的原子性方法<br/>
	 * 
	 * @author cpthack cpt@jianzhimao.com
	 * @param message
	 *            队列中取出的数据
	 * @return boolean
	 *
	 */
	boolean run(String message);
}
