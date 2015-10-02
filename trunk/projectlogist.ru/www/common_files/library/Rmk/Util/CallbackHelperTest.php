<?php
/**
 * Автотест помощника для работы с функциями обратного вызова.
 *
 * @category Rmk
 * @package  Rmk_Util
 * @author   Roman rmk Mogilatov rmogilatov@gmail.com
 */

namespace RmkTest\Util;

use Rmk\Util\CallbackHelper,
    PHPUnit_Framework_TestCase;

class CallbackHelperTest extends PHPUnit_Framework_TestCase
{

    public function testIsCallback()
    {
        $closureCallback = function() {

                };
        $functionCallback     = 'md5';
        $staticMethodCallback = array('Rmk\Util\ArrayHelper', 'getByKey');
        $objectMethodCallback = array(&$this, 'assertTrue');

        $int    = 0;
        $float  = 0.0;
        $string = '';
        $array  = array();

        $this->assertTrue(CallbackHelper::isCallable($closureCallback));
        $this->assertTrue(CallbackHelper::isCallable($functionCallback));
        $this->assertTrue(CallbackHelper::isCallable($staticMethodCallback));
        $this->assertTrue(CallbackHelper::isCallable($objectMethodCallback));

        $this->assertFalse(CallbackHelper::isCallable($int));
        $this->assertFalse(CallbackHelper::isCallable($float));
        $this->assertFalse(CallbackHelper::isCallable($string));
        $this->assertFalse(CallbackHelper::isCallable($array));
    }

    public function testGetType()
    {
        $closureCallback = function() {

                };
        $functionCallback     = 'md5';
        $staticMethodCallback = array('Rmk\Util\ArrayHelper', 'getByKey');
        $objectMethodCallback = array(&$this, 'assertTrue');

        $int = 0;

        $this->assertEquals(CallbackHelper::CALLBACK_CLOSURE,
                            CallbackHelper::getType($closureCallback));
        $this->assertEquals(CallbackHelper::CALLBACK_FUNCTION,
                            CallbackHelper::getType($functionCallback));
        $this->assertEquals(CallbackHelper::CALLBACK_STATIC_METHOD,
                            CallbackHelper::getType($staticMethodCallback));
        $this->assertEquals(CallbackHelper::CALLBACK_OBJECT_METHOD,
                            CallbackHelper::getType($objectMethodCallback));

        $this->setExpectedException('UnexpectedValueException');

        CallbackHelper::getType($int);
    }

    public function testEnsureCallable()
    {
        $callback = 'md5';

        $ensuredCallback = &CallbackHelper::ensureCallable($callback);

        $ensuredCallback = 'strcmp';

        $this->assertEquals('strcmp', $callback);

        CallbackHelper::ensureCallable($ensuredCallback);
    }

    public function testEnsureCallableNotCallable()
    {
        $this->setExpectedException('UnexpectedValueException');

        $callback = 'abracadabra';

        CallbackHelper::ensureCallable($callback);
    }

    public function testEnsureCallableNotCallback()
    {
        $this->setExpectedException('UnexpectedValueException');

        $callback = 25;

        CallbackHelper::ensureCallable($callback);
    }

    public function testEnsureType()
    {
        $callback = 'md5';

        $ensuredCallback = &CallbackHelper::ensureType($callback,
                                                       CallbackHelper::CALLBACK_FUNCTION);

        $ensuredCallback = 'strcmp';

        $this->assertEquals('strcmp', $callback);

        CallbackHelper::ensureType($ensuredCallback,
                                   CallbackHelper::CALLBACK_FUNCTION);

        $this->setExpectedException('UnexpectedValueException');

        CallbackHelper::ensureType($ensuredCallback,
                                   CallbackHelper::CALLBACK_CLOSURE);
    }

}