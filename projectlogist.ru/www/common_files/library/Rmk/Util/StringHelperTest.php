<?php
/**
 * Автотест помощника для работы со строками.
 *
 * @category Rmk
 * @package  Rmk_Util
 * @author   Roman rmk Mogilatov rmogilatov@gmail.com
 */

namespace RmkTest\Util;

use Rmk\Util\StringHelper,
    PHPUnit_Framework_TestCase;

class StringHelperTest extends PHPUnit_Framework_TestCase
{

    public function testIsString()
    {
        $string = 'std';
        $int    = 25;
        $array  = array();

        $this->assertTrue(StringHelper::isString($string));

        $this->assertFalse(StringHelper::isString($int));
        $this->assertFalse(StringHelper::isString($array));
    }

    public function testEnsureString()
    {
        $string = 'std';

        $ensuredString = &StringHelper::ensureString($string);

        $ensuredString = 'eStd';

        $this->assertEquals('eStd', $string);
    }

    public function testEnsureStringException()
    {
        $this->setExpectedException('UnexpectedValueException');

        $mayBeString = 1;

        StringHelper::ensureString($mayBeString);
    }

}