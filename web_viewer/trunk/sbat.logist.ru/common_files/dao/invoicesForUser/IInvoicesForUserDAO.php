<?php

interface IInvoicesForUserDAO
{
    function selectAllData();

    function selectDataByKey($keyword);
}