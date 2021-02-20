package cn.wangoon.common.utils;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Description Redis 工具类
 * @PackagePath cn.wangoon.common.utils.RedisUtils
 * @Author YINZHIYU
 * @Date 2020-04-10 18:37:00
 * @Version 1.0.0.0
 **/
@SuppressWarnings("unchecked")
@Component
public class RedisUtils {
    @Resource
    public RedisTemplate<String, Object> redisTemplate;

    /**
     * @Description 加锁
     * @Params ==>
     * @Param key 唯一键
     * @Param value 这里是时间戳 + 超时时间(毫秒)
     * @Return boolean true 已锁  false未锁
     * @Date 2020/4/21 9:44
     * @Auther YINZHIYU
     */
    public boolean setLock(String key, String value) {
        try {
            // 对应setnx命令，如果key不存在则新增,返回true,存在则不改变已经有的值,返回false。
            if (redisTemplate.opsForValue().setIfAbsent(key, value)) {
                //可以成功设置,也就是key不存在
                return true;
            }

            // 获取上一个锁的时间value
            String currentValue = CastUtil.cast(redisTemplate.opsForValue().get(key));

            // 判断锁超时 -> 防止原来的操作异常，没有运行解锁操作  防止死锁
            // currentValue 不为空且小于当前时间，则锁过期
            if (ObjectUtil.isNotEmpty(currentValue) && Long.parseLong(currentValue) < System.currentTimeMillis()) {

                // 对应getset命令，如果key存在，返回key的对应的值并重新设置新的值
                // redis是单线程处理，即使并发存在，这里的getAndSet也是单个执行

                // 假设两个线程同时进来，key已被占用。获取的值currentValue=A(get取的旧的值肯定是一样的),
                // 两个线程的value都是B,key一样.锁时间已经过期了。
                // 而这里面的getAndSet一次只会一个执行，也就是一个执行之后，上一个的value已经变成了B。只有一个线程获取的上一个值会是A，另一个线程拿到的值是B。
                // 由此解决并发问题
                String oldValue = CastUtil.cast(redisTemplate.opsForValue().getAndSet(key, value));
                return ObjectUtil.isNotEmpty(oldValue) && StrUtil.equals(oldValue, currentValue);
            }
        } catch (Exception e) {
            LogUtils.error(key, value, "操作Redis ==> 异常", e);
            return false;
        }
        return false;
    }

    /**
     * @Description 释放锁[释放锁 true 已释放  false 未释放]
     * @Params ==>
     * @Param key 唯一键
     * @Param value 这里是时间戳
     * @Return boolean
     * @Date 2020/4/21 9:43
     * @Auther YINZHIYU
     */
    public boolean releaseLock(String key, String value) {
        try {
            String currentValue = CastUtil.cast(redisTemplate.opsForValue().get(key));
            if (ObjectUtil.isNotEmpty(currentValue) && StrUtil.equals(currentValue, value)) {
                return redisTemplate.opsForValue().getOperations().delete(key);// 删除key
            }
        } catch (Exception e) {
            LogUtils.error(key, value, "操作Redis ==> 异常", e);
            return false;
        }
        return false;
    }

    //- - - - - - - - - - - - - - - - - - - - -  公共方法 - - - - - - - - - - - - - - - - - - - -

    /**
     * @Description 给一个指定的 key 值附加过期时间
     * @Params ==>
     * @Param null
     * @Return
     * @Date 2020/4/21 9:45
     * @Auther YINZHIYU
     */
    public boolean expire(String key, long time) {
        return redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

    /**
     * @Description 给一个指定的 key 值附加过期时间
     * @Params ==>
     * @Param key
     * @Param time
     * @Param timeUnit[秒/分/时]
     * @Return boolean
     * @Date 2020/5/26 18:42
     * @Auther YINZHIYU
     */
    public boolean expire(String key, long time, TimeUnit timeUnit) {
        return redisTemplate.expire(key, time, timeUnit);
    }

    /**
     * @Description 给一个指定的 key 值附加过期时间
     * @Params ==>
     * @Param key
     * @Param date
     * @Return boolean
     * @Date 2020/5/26 18:41
     * @Auther YINZHIYU
     */
    public boolean expireAt(String key, Date date) {
        return redisTemplate.expireAt(key, date);
    }

    /**
     * @Description 根据key 获取过期时间
     * @Params ==>
     * @Param key key 键 不能为null
     * @Return long 时间(秒) 返回0代表为永久有效
     * @Date 2020/4/21 9:45
     * @Auther YINZHIYU
     */
    public long getTime(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * @Description 根据key 获取过期时间
     * @Params ==>
     * @Param key
     * @Return boolean
     * @Date 2020/4/21 9:45
     * @Auther YINZHIYU
     */
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * @Description 移除指定key 的过期时间
     * @Params ==>
     * @Param key
     * @Return boolean
     * @Date 2020/4/21 9:46
     * @Auther YINZHIYU
     */
    public boolean persist(String key) {
        return redisTemplate.boundValueOps(key).persist();
    }

    //- - - - - - - - - - - - - - - - - - - - -  String类型 - - - - - - - - - - - - - - - - - - - -

    /**
     * @Description 根据key获取值
     * @Params ==>
     * @Param key
     * @Return java.lang.Object
     * @Date 2020/4/21 9:47
     * @Auther YINZHIYU
     */
    public Object get(String key) {
        return ObjectUtil.isEmpty(key) ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * @Description 根据key获取值
     * [泛型方法，运行时确定返回值，相较于上面get 返回Object需要用户自行强制转换，此处还是优越得多]
     * [泛型方法的<T> 与 泛型类的<T> 是2个不同的泛型类型]
     * @Params ==>
     * @Param key
     * @Return T
     * @Date 2020/5/18 10:05
     * @Auther YINZHIYU
     */
    public <T> T getValue(String key) {
        return ObjectUtil.isEmpty(key) ? null : (T) redisTemplate.opsForValue().get(key);
    }

    /**
     * @Description 将值放入缓存
     * @Params ==>
     * @Param key
     * @Param value
     * @Return void
     * @Date 2020/4/21 9:48
     * @Auther YINZHIYU
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * @Description 将值放入缓存并设置时间
     * @Params ==>
     * @Param key
     * @Param value
     * @Param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @Return void
     * @Date 2020/4/21 9:48
     * @Auther YINZHIYU
     */
    public void set(String key, Object value, long time) {
        if (time > 0) {
            redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
        } else {
            redisTemplate.opsForValue().set(key, value);
        }
    }

    /**
     * @Description 批量添加 key (重复的键会覆盖)
     * @Params ==>
     * @Param keyAndValue
     * @Return void
     * @Date 2020/4/21 9:49
     * @Auther YINZHIYU
     */
    public void batchSet(Map<String, Object> keyAndValue) {
        redisTemplate.opsForValue().multiSet(keyAndValue);
    }

    /**
     * @Description 批量添加 key-value 只有在键不存在时,才添加 map 中只要有一个key存在,则全部不添加
     * @Params ==>
     * @Param keyAndValue
     * @Return void
     * @Date 2020/4/21 9:49
     * @Auther YINZHIYU
     */
    public void batchSetIfAbsent(Map<String, Object> keyAndValue) {
        redisTemplate.opsForValue().multiSetIfAbsent(keyAndValue);
    }

    /**
     * @Description 保存  key 的值设为 value ，当且仅当 key 不存在,并设置有效期.(防止重复提交)
     * @Remark
     * @Params ==>
     * @Param key
     * @Param value
     * @Param timeOut
     * @Param unit
     * @Return boolean
     * @Date 2020/11/17 13:54
     * @Auther YINZHIYU
     */
    public boolean setIfAbsent(String key, Object value, Integer timeOut, TimeUnit unit) {
        return redisTemplate.opsForValue().setIfAbsent(key, value, timeOut, unit);
    }

    /**
     * @Description 对一个 key-value 的值进行加减操作,如果该 key 不存在 将创建一个key 并赋值该 number,如果 key 存在,但 value 不是长整型 ,将报错
     * @Params ==>
     * @Param key
     * @Param number 正负数 正为加 负为减
     * @Return java.lang.Long
     * @Date 2020/4/21 9:49
     * @Auther YINZHIYU
     */
    public Long increment(String key, long number) {
        return redisTemplate.opsForValue().increment(key, number);
    }

    /**
     * @Description 对一个 key-value 的值进行加减操作, 如果该 key 不存在 将创建一个key 并赋值该 number 如果 key 存在,但 value 不是 纯数字 ,将报错
     * @Params ==>
     * @Param key
     * @Param number 正负数 正为加 负为减
     * @Return java.lang.Double
     * @Date 2020/4/21 9:50
     * @Auther YINZHIYU
     */
    public Double increment(String key, double number) {
        return redisTemplate.opsForValue().increment(key, number);
    }

    //- - - - - - - - - - - - - - - - - - - - -  set类型 - - - - - - - - - - - - - - - - - - - -

    /**
     * @Description 将数据放入set缓存
     * @Params ==>
     * @Param key
     * @Param value
     * @Return void
     * @Date 2020/4/21 9:50
     * @Auther YINZHIYU
     */
    public void sSet(String key, Object value) {
        redisTemplate.opsForSet().add(key, value);
    }

    /**
     * @Description 获取变量中的值
     * @Params ==>
     * @Param key
     * @Return java.util.Set<java.lang.Object>
     * @Date 2020/4/21 9:50
     * @Auther YINZHIYU
     */
    public Set<Object> members(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * @Description 随机获取变量中指定个数的元素
     * @Params ==>
     * @Param key
     * @Param count
     * @Return void
     * @Date 2020/4/21 9:50
     * @Auther YINZHIYU
     */
    public void randomMembers(String key, long count) {
        redisTemplate.opsForSet().randomMembers(key, count);
    }

    /**
     * @Description 随机获取变量中的元素
     * @Params ==>
     * @Param key
     * @Return java.lang.Object
     * @Date 2020/4/21 9:50
     * @Auther YINZHIYU
     */
    public Object randomMember(String key) {
        return redisTemplate.opsForSet().randomMember(key);
    }

    /**
     * @Description 弹出变量中的元素
     * @Params ==>
     * @Param key
     * @Return java.lang.Object
     * @Date 2020/4/21 9:51
     * @Auther YINZHIYU
     */
    public Object pop(String key) {
        return redisTemplate.opsForSet().pop("setValue");
    }

    /**
     * @Description 获取变量中值的长度
     * @Params ==>
     * @Param key
     * @Return long
     * @Date 2020/4/21 9:51
     * @Auther YINZHIYU
     */
    public long sizeForSet(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    /**
     * @Description 根据value从一个set中查询, 是否存在
     * @Params ==>
     * @Param key
     * @Param value
     * @Return boolean  true 存在 false不存在
     * @Date 2020/4/21 9:51
     * @Auther YINZHIYU
     */
    public boolean sHasKey(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    /**
     * @Description 检查给定的元素是否在变量中
     * @Params ==>
     * @Param key
     * @Param obj
     * @Return boolean
     * @Date 2020/4/21 9:51
     * @Auther YINZHIYU
     */
    public boolean isMember(String key, Object obj) {
        return redisTemplate.opsForSet().isMember(key, obj);
    }

    /**
     * @Description 转移变量的元素值到目的变量
     * @Params ==>
     * @Param key
     * @Param value
     * @Param destKey
     * @Return boolean
     * @Date 2020/4/21 9:52
     * @Auther YINZHIYU
     */
    public boolean move(String key, Object value, String destKey) {
        return redisTemplate.opsForSet().move(key, value, destKey);
    }

    /**
     * @Description 批量移除set缓存中元素
     * @Params ==>
     * @Param key
     * @Param values
     * @Return void
     * @Date 2020/4/21 9:52
     * @Auther YINZHIYU
     */
    public void remove(String key, Object... values) {
        redisTemplate.opsForSet().remove(key, values);
    }

    //- - - - - - - - - - - - - - - - - - - - -  hash类型 - - - - - - - - - - - - - - - - - - - -

    /**
     * @Description 加入缓存
     * @Params ==>
     * @Param key
     * @Param map
     * @Return void
     * @Date 2020/4/21 9:52
     * @Auther YINZHIYU
     */
    public void add(String key, Map<String, Object> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * @Description 加入缓存, 并设置超时时间
     * @Params ==>
     * @Param key
     * @Param map
     * @Param time  时间(秒)
     * @Return void
     * @Date 2020/6/12 10:11
     * @Auther YINZHIYU
     */
    public void add(String key, Map<String, Object> map, long time) {
        redisTemplate.opsForHash().putAll(key, map);
        if (time > 0) {
            redisTemplate.expire(key, time, TimeUnit.SECONDS);
        }
    }

    /**
     * @Description 加入缓存, 并设置超时时间
     * @Params ==>
     * @Param key
     * @Param hashKey
     * @Param object
     * @Return void
     * @Date 2020/5/15 13:58
     * @Auther YINZHIYU
     */
    public void add(String key, Object hashKey, Object object) {
        redisTemplate.opsForHash().put(key, hashKey, object);
    }

    /**
     * @Description 加入缓存
     * @Params ==>
     * @Param key
     * @Param hashKey
     * @Param object
     * @Param time   时间(秒)
     * @Return void
     * @Date 2020/6/12 10:14
     * @Auther YINZHIYU
     */
    public void add(String key, Object hashKey, Object object, long time) {
        redisTemplate.opsForHash().put(key, hashKey, object);
        if (time > 0) {
            redisTemplate.expire(key, time, TimeUnit.SECONDS);
        }
    }


    /**
     * @Description 获取缓存
     * @Params ==>
     * @Param key
     * @Param hashKey
     * @Return java.lang.Object
     * @Date 2020/5/15 14:00
     * @Auther YINZHIYU
     */
    public Object get(String key, Object hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * @Description 获取缓存
     * @Params ==>
     * @Param key
     * @Param hashKey
     * @Return java.lang.Object
     * @Date 2020/5/15 14:00
     * @Auther YINZHIYU
     */
    public <T> T getValues(String key) {
        return (T) redisTemplate.opsForHash().values(key);
    }

    /**
     * @Description 获取缓存
     * @Params ==>
     * @Param key
     * @Param hashKey
     * @Return java.lang.Object
     * @Date 2020/5/15 14:00
     * @Auther YINZHIYU
     */
    public <T> T getValue(String key, Object hashKey) {
        return key == null ? null : (T) redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * @Description 获取 key 下的 所有  hashkey 和 value
     * @Params ==>
     * @Param key
     * @Return java.util.Map<java.lang.Object, java.lang.Object>
     * @Date 2020/4/21 9:53
     * @Auther YINZHIYU
     */
    public Map<Object, Object> getHashEntries(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * @Description 验证指定 key 下 有没有指定的 hashkey
     * @Params ==>
     * @Param key
     * @Param hashKey
     * @Return boolean
     * @Date 2020/4/21 9:53
     * @Auther YINZHIYU
     */
    public boolean hashKey(String key, String hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    /**
     * @Description 获取指定key的值string
     * @Params ==>
     * @Param key
     * @Param key2
     * @Return java.lang.String
     * @Date 2020/4/21 9:53
     * @Auther YINZHIYU
     */
    public String getMapString(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey).toString();
    }

    /**
     * @Description 获取指定的值Int
     * @Params ==>
     * @Param key
     * @Param hashKey
     * @Return java.lang.Integer
     * @Date 2020/4/21 9:53
     * @Auther YINZHIYU
     */
    public Integer getMapInt(String key, String hashKey) {
        return (Integer) redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * @Description 弹出元素并删除
     * @Params ==>
     * @Param key
     * @Return java.lang.String
     * @Date 2020/4/21 9:54
     * @Auther YINZHIYU
     */
    public String popValue(String key) {
        return redisTemplate.opsForSet().pop(key).toString();
    }

    /**
     * @Description 删除指定
     * @Params ==>
     * @Param key
     * @Return java.lang.Boolean
     * @Date 2020/4/21 9:54
     * @Auther YINZHIYU
     */
    public Boolean delete(String key) {
        return redisTemplate.opsForValue().getOperations().delete(key);// 删除key
    }

    /**
     * @Description 删除缓存
     * @Params ==>
     * @Param key 可以传一个值 或多个
     * @Return void
     * @Date 2020/6/12 9:56
     * @Auther YINZHIYU
     */
    public void delete(String... key) {

        if (ObjectUtil.isNotEmpty(key)) {
            if (key.length == 1) {
                redisTemplate.opsForValue().getOperations().delete(key[0]);
            } else {
                redisTemplate.opsForValue().getOperations().delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    /**
     * @Description 删除指定 hash 的 HashKey
     * @Params ==>
     * @Param key
     * @Param hashKeys
     * @Return java.lang.Long  删除成功的 数量
     * @Date 2020/4/21 9:54
     * @Auther YINZHIYU
     */
    public Long delete(String key, Object... hashKeys) {
        return redisTemplate.opsForHash().delete(key, hashKeys);
    }

    /**
     * @Description 给指定 hash 的 hashkey 做增减操作
     * @Params ==>
     * @Param key
     * @Param hashKey
     * @Param number
     * @Return java.lang.Long
     * @Date 2020/4/21 9:55
     * @Auther YINZHIYU
     */
    public Long increment(String key, Object hashKey, long number) {
        return redisTemplate.opsForHash().increment(key, hashKey, number);
    }

    /**
     * @Description 给指定 hash 的 hashkey 做增减操作
     * @Params ==>
     * @Param key
     * @Param hashKey
     * @Param number
     * @Return java.lang.Double
     * @Date 2020/4/21 9:55
     * @Auther YINZHIYU
     */
    public Double increment(String key, Object hashKey, Double number) {
        return redisTemplate.opsForHash().increment(key, hashKey, number);
    }

    /**
     * @Description 获取 key 下的 所有 hashkey 字段
     * @Params ==>
     * @Param key
     * @Return java.util.Set<java.lang.Object>
     * @Date 2020/4/21 9:55
     * @Auther YINZHIYU
     */
    public Set<Object> hashKeys(String key) {
        return redisTemplate.opsForHash().keys(key);
    }

    /**
     * @Description 获取指定 hash 下面的 键值对 数量
     * @Params ==>
     * @Param key
     * @Return java.lang.Long
     * @Date 2020/4/21 9:56
     * @Auther YINZHIYU
     */
    public Long hashSize(String key) {
        return redisTemplate.opsForHash().size(key);
    }

    //- - - - - - - - - - - - - - - - - - - - -  list类型 - - - - - - - - - - - - - - - - - - - -

    /**
     * @Description 在变量左边添加元素值
     * @Params ==>
     * @Param key
     * @Param value
     * @Return void
     * @Date 2020/4/21 9:56
     * @Auther YINZHIYU
     */
    public void leftPush(String key, Object value) {
        redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * @Description 获取集合指定位置的值
     * @Params ==>
     * @Param key
     * @Param index
     * @Return java.lang.Object
     * @Date 2020/4/21 9:56
     * @Auther YINZHIYU
     */
    public Object index(String key, long index) {
        return redisTemplate.opsForList().index(key, index);
    }

    /**
     * @Description 获取指定区间的值
     * @Params ==>
     * @Param key
     * @Param start
     * @Param end
     * @Return java.util.List<java.lang.Object>
     * @Date 2020/4/21 9:56
     * @Auther YINZHIYU
     */
    public List<Object> range(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * @Description 把最后一个参数值放到指定集合的第一个出现中间参数的前面， 如果中间参数值存在的话。
     * @Params ==>
     * @Param key
     * @Param pivot
     * @Param value
     * @Return void
     * @Date 2020/4/21 9:56
     * @Auther YINZHIYU
     */
    public void leftPush(String key, Object pivot, Object value) {
        redisTemplate.opsForList().leftPush(key, pivot, value);
    }

    /**
     * @Description 向左边批量添加参数元素
     * @Params ==>
     * @Param key
     * @Param values
     * @Return void
     * @Date 2020/4/21 9:56
     * @Auther YINZHIYU
     */
    public void leftPushAll(String key, Object... values) {
//        redisTemplate.opsForList().leftPushAll(key,"w","x","y");
        redisTemplate.opsForList().leftPushAll(key, values);
    }

    /**
     * @Description 向集合最右边添加元素
     * @Params ==>
     * @Param key
     * @Param value
     * @Return void
     * @Date 2020/4/21 9:57
     * @Auther YINZHIYU
     */
    public void leftPushAll(String key, Object value) {
        redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * @Description 向左边批量添加参数元素
     * @Params ==>
     * @Param key
     * @Param values
     * @Return void
     * @Date 2020/4/21 9:57
     * @Auther YINZHIYU
     */
    public void rightPushAll(String key, Object... values) {
//        redisTemplate.opsForList().leftPushAll(key,"w","x","y");
        redisTemplate.opsForList().rightPushAll(key, values);
    }

    /*
     * @Description 向已存在的集合中添加元素
     * @Params ==>
     * @Param key
     * @Param value
     * @Return void
     * @Date 2020/4/21 9:57
     * @Auther YINZHIYU
     */
    public void rightPushIfPresent(String key, Object value) {
        redisTemplate.opsForList().rightPushIfPresent(key, value);
    }

    /**
     * @Description 向已存在的集合中添加元素
     * @Params ==>
     * @Param key
     * @Return long
     * @Date 2020/4/21 9:57
     * @Auther YINZHIYU
     */
    public long listLength(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /**
     * @Description 移除集合中的左边第一个元素
     * @Params ==>
     * @Param key
     * @Return void
     * @Date 2020/4/21 9:57
     * @Auther YINZHIYU
     */
    public void leftPop(String key) {
        redisTemplate.opsForList().leftPop(key);
    }

    /**
     * @Description 移除集合中左边的元素在等待的时间里，如果超过等待的时间仍没有元素则退出
     * @Params ==>
     * @Param key
     * @Param timeout
     * @Param unit
     * @Return void
     * @Date 2020/4/21 9:57
     * @Auther YINZHIYU
     */
    public void leftPop(String key, long timeout, TimeUnit unit) {
        redisTemplate.opsForList().leftPop(key, timeout, unit);
    }

    /**
     * @Description 移除集合中右边的元素
     * @Params ==>
     * @Param key
     * @Return void
     * @Date 2020/4/21 9:57
     * @Auther YINZHIYU
     */
    public void rightPop(String key) {
        redisTemplate.opsForList().rightPop(key);
    }

    /**
     * @Description 移除集合中右边的元素在等待的时间里，如果超过等待的时间仍没有元素则退出
     * @Params ==>
     * @Param key
     * @Param timeout
     * @Param unit
     * @Return void
     * @Date 2020/4/21 9:58
     * @Auther YINZHIYU
     */
    public void rightPop(String key, long timeout, TimeUnit unit) {
        redisTemplate.opsForList().rightPop(key, timeout, unit);
    }

    /**
     * @Description 获取变量中值的长度
     * @Params ==>
     * @Param key
     * @Return long
     * @Date 2020/4/21 9:51
     * @Auther YINZHIYU
     */
    public long sizeForList(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /**
     * @Description Redis 记录日志，默认保留5天
     * @Params ==>
     * @Param key
     * @Param value
     * @Param days
     * @Return void
     * @Date 2020/5/15 11:45
     * @Auther YINZHIYU
     */
    public void recordLogs(String key, Object value, final Integer days) {
        ThreadUtil.execAsync(() -> {
            try {
                rightPushAll(key, value);
                if (days == null || days <= 0) {
                    expire(key, 5 * 24 * 3600);
                } else {
                    expire(key, days * 24 * 3600);
                }
            } catch (Exception e) {
                LogUtils.error(key, value, days, "操作Redis ==> 异常", e);
            }
        });
    }

    /**
     * @Description Redis 记录日志，默认保留5天
     * @Params ==>
     * @Param key
     * @Param value
     * @Param days
     * @Return void
     * @Date 2020/5/15 11:45
     * @Auther YINZHIYU
     */
    public void recordLogs(String key, String hashKey, Object value, final Integer days) {
        ThreadUtil.execAsync(() -> {
            try {
                List list = CastUtil.cast(get(key, hashKey));
                if (ObjectUtil.isEmpty(list)) {
                    list = Lists.newArrayList();
                }
                list.add(value);
                add(key, hashKey, list);
                if (days == null || days <= 0) {
                    expire(key, 5 * 24 * 3600);
                } else {
                    expire(key, days * 24 * 3600);
                }
            } catch (Exception e) {
                LogUtils.error(key, hashKey, value, days, "操作Redis ==> 异常", e);
            }
        });
    }
}
