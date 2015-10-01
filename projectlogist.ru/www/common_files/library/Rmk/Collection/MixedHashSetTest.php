<?php
/**
 * Автотест хешированной карты общего назначения.
 *
 * @category Rmk
 * @package  Rmk_Collection
 * @author   Roman rmk Mogilatov rmogilatov@gmail.com
 */

namespace RmkTest\Collection;

use Rmk\Collection\MixedHashSet,
    stdClass,
    Exception,
    PHPUnit_Framework_TestCase;

class MixedHashSetTest extends PHPUnit_Framework_TestCase
{

    /**
     * Тестовые данные.
     *
     * @var array
     */
    protected $array;

    /**
     * Список общего назначения.
     *
     * @var MixedHashSet
     */
    protected $set;

    public function setUp()
    {
        $this->set = new MixedHashSet();

        $this->array = array(
            'v1' => 'v1',
            'v2' => 'v2',
            'v3' => 'v3'
        );

        $this->set->fromArray($this->array);
    }

    public function tearDown()
    {
        $this->set->clear();
    }

    public function testAdd()
    {
        $this->set
                ->add('v1')
                ->add('v2')
                ->add('v3');

        $this->assertEquals($this->array, $this->set->toArray());
    }

    public function testSet()
    {
        $this->set
                ->set('v1', 'v1')
                ->set('v2', 'v2')
                ->set('v3', 'v3');

        $this->setExpectedException('UnexpectedValueException');

        $this->set->set('v11', 'v1');
    }

    public function testContains()
    {
        $this->assertTrue($this->set->contains('v1'));
        $this->assertTrue($this->set->contains('v2'));
        $this->assertTrue($this->set->contains('v3'));

        $this->assertFalse($this->set->contains('v4'));
    }

    public function testRemove()
    {
        $this->set
                ->remove('v1')
                ->remove('v2')
                ->remove('v3');

        $this->assertEmpty($this->set->toArray());
    }

    public function testKeyOf()
    {
        $this->assertEquals('v1', $this->set->keyOf('v1'));
        $this->assertEquals('v2', $this->set->keyOf('v2'));
        $this->assertEquals('v3', $this->set->keyOf('v3'));

        $this->setExpectedException('UnexpectedValueException');

        $this->set->keyOf('v4');
    }

    public function testLastKeyOf()
    {
        $this->assertEquals('v1', $this->set->lastKeyOf('v1'));
        $this->assertEquals('v2', $this->set->lastKeyOf('v2'));
        $this->assertEquals('v3', $this->set->lastKeyOf('v3'));

        $this->setExpectedException('UnexpectedValueException');

        $this->set->lastKeyOf('v4');
    }

    public function testGetKey()
    {
        $object    = new stdClass();
        $exception = new Exception();

        $this->assertEquals(spl_object_hash($object),
                                            $this->set->getKey($object));

        $this->assertEquals($exception->__toString(),
                            $this->set->getKey($exception));
    }

}