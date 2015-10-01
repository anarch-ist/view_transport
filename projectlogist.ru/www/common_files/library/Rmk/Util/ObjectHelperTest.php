<?php
/**
 * Автотест помощника для работы с объектами.
 *
 * @category Rmk
 * @package  Rmk_Util
 * @author   Roman rmk Mogilatov rmogilatov@gmail.com
 */

namespace RmkTest\Util;

use Rmk\Util\ObjectHelper,
    PHPUnit_Framework_TestCase,
    stdClass;

class ObjectHelperTest extends PHPUnit_Framework_TestCase
{

    public function testIsObject()
    {
        $object = new stdClass();

        $int    = 0;
        $float  = 0.0;
        $string = '';
        $array  = array();

        $this->assertTrue(ObjectHelper::isObject($object));

        $this->assertFalse(ObjectHelper::isObject($int));
        $this->assertFalse(ObjectHelper::isObject($float));
        $this->assertFalse(ObjectHelper::isObject($string));
        $this->assertFalse(ObjectHelper::isObject($array));
    }

    public function testIsInstanceOf()
    {
        $object = new stdClass();

        $int    = 0;
        $float  = 0.0;
        $string = '';
        $array  = array();

        $this->assertTrue(ObjectHelper::isInstanceOf($object, 'stdClass'));

        $this->assertFalse(ObjectHelper::isInstanceOf($object, 'Exception'));
        $this->assertFalse(ObjectHelper::isInstanceOf($int, 'Exception'));
        $this->assertFalse(ObjectHelper::isInstanceOf($float, 'Exception'));
        $this->assertFalse(ObjectHelper::isInstanceOf($string, 'Exception'));
        $this->assertFalse(ObjectHelper::isInstanceOf($array, 'Exception'));
    }

    public function testEnsureObject()
    {
        $object = new stdClass();
        $object->data = 'data1';

        $ensuredObject = ObjectHelper::ensureObject($object);

        $ensuredObject->data = 'data2';

        $this->assertEquals('data2', $object->data);

        $this->setExpectedException('UnexpectedValueException');

        $mayBeObject = '';

        ObjectHelper::ensureObject($mayBeObject);
    }

    public function testEnsureInstanceOf()
    {
        $object = new stdClass();
        $object->data = 'data1';

        $ensuredObject = ObjectHelper::ensureInstanceOf($object, 'stdClass');

        $ensuredObject->data = 'data2';

        $this->assertEquals('data2', $object->data);

        $this->setExpectedException('UnexpectedValueException');

        $mayBeObject = '';

        ObjectHelper::ensureInstanceOf($mayBeObject, 'stdClass');
    }

}