<?php
/**
 * Автотест помощника для работы с пустыми значениями.
 *
 * @category Rmk
 * @package  Rmk_Util
 * @author   Roman rmk Mogilatov rmogilatov@gmail.com
 */

namespace RmkTest\Util;

use Rmk\Util\NullHelper,
    PHPUnit_Framework_TestCase;

class NullHelperTest extends PHPUnit_Framework_TestCase
{

    public function testIsNull()
    {
        $null = null;

        $false  = false;
        $int    = 0;
        $float  = 0.0;
        $string = '';
        $array  = array();

        $this->assertTrue(NullHelper::isNull($null));

        $this->assertFalse(NullHelper::isNull($false));
        $this->assertFalse(NullHelper::isNull($int));
        $this->assertFalse(NullHelper::isNull($float));
        $this->assertFalse(NullHelper::isNull($string));
        $this->assertFalse(NullHelper::isNull($array));
    }

    public function testIsNotNull()
    {
        $null = null;

        $false  = false;
        $int    = 0;
        $float  = 0.0;
        $string = '';
        $array  = array();

        $this->assertFalse(NullHelper::isNotNull($null));

        $this->assertTrue(NullHelper::isNotNull($false));
        $this->assertTrue(NullHelper::isNotNull($int));
        $this->assertTrue(NullHelper::isNotNull($float));
        $this->assertTrue(NullHelper::isNotNull($string));
        $this->assertTrue(NullHelper::isNotNull($array));
    }

    public function testEnsureNull()
    {
        $null = null;

        $ensuredNull = &NullHelper::ensureNull($null);

        $ensuredNull = 1;

        $this->assertEquals(1, $null);

        $this->setExpectedException('UnexpectedValueException');

        NullHelper::ensureNull($null);
    }

    public function testEnsureNotNull()
    {
        $notNull = 1;

        $ensuredNotNull = &NullHelper::ensureNotNull($notNull);

        $ensuredNotNull = null;

        $this->assertEquals(null, $notNull);

        $this->setExpectedException('UnexpectedValueException');

        NullHelper::ensureNotNull($notNull);
    }

//    public function testIsTrue()
//    {
//        $true  = true;
//        $false = false;
//
//        $int    = 0;
//        $float  = 0.0;
//        $string = '';
//        $array  = array();
//
//        $this->assertTrue(BoolHelper::isTrue($true));
//
//        $this->assertFalse(BoolHelper::isTrue($false));
//        $this->assertFalse(BoolHelper::isTrue($int));
//        $this->assertFalse(BoolHelper::isTrue($float));
//        $this->assertFalse(BoolHelper::isTrue($string));
//        $this->assertFalse(BoolHelper::isTrue($array));
//    }
//
//    public function testIsFalse()
//    {
//        $true  = true;
//        $false = false;
//
//        $int    = 0;
//        $float  = 0.0;
//        $string = '';
//        $array  = array();
//
//        $this->assertTrue(BoolHelper::isFalse($false));
//
//        $this->assertFalse(BoolHelper::isFalse($true));
//        $this->assertFalse(BoolHelper::isFalse($int));
//        $this->assertFalse(BoolHelper::isFalse($float));
//        $this->assertFalse(BoolHelper::isFalse($string));
//        $this->assertFalse(BoolHelper::isFalse($array));
//    }
//
//    public function testEnsureBool()
//    {
//        $bool = true;
//
//        $ensuredBool = &BoolHelper::ensureBool($bool);
//
//        $ensuredBool = false;
//
//        $this->assertEquals(false, $bool);
//
//        BoolHelper::ensureBool($ensuredBool);
//
//        $this->setExpectedException('UnexpectedValueException');
//
//        $mayBeBool = array();
//
//        BoolHelper::ensureBool($mayBeBool);
//    }
//
//    public function testEnsureTrue()
//    {
//        $true = true;
//
//        $ensuredTrue = &BoolHelper::ensureTrue($true);
//
//        $ensuredTrue = false;
//
//        $this->assertEquals(false, $true);
//
//        $this->setExpectedException('UnexpectedValueException');
//
//        $mayBeBool = array();
//
//        BoolHelper::ensureTrue($mayBeBool);
//    }
//
//    public function testEnsureFalse()
//    {
//        $false = false;
//
//        $ensuredFalse = &BoolHelper::ensureFalse($false);
//
//        $ensuredFalse = true;
//
//        $this->assertEquals(true, $false);
//
//        $this->setExpectedException('UnexpectedValueException');
//
//        $mayBeBool = array();
//
//        BoolHelper::ensureFalse($mayBeBool);
//    }
}