'use client'

import {
    Heading,
    Avatar,
    Box,
    Center,
    Image,
    Flex,
    Text,
    Stack,
    Tag,
    useColorModeValue,
} from '@chakra-ui/react'
import React from 'react';

import UpdateCustomerDrawer from "./UpdateCustomerDrawer.jsx";
import DeleteCustomer from "./DeleteCustomer.jsx";

export default function CartWithImage({id, name, email, age, gender, imageNumber, fetchCustomers}) {
    const randomUserGender = gender === 'MALE' ? 'men' : 'women'



    return (
        <Center py={6}>
            <Box
                maxW={'300px'}
                minW={'300px'}
                w={'full'}
                m={2}
                bg={useColorModeValue('white', 'gray.800')}
                boxShadow={'lg'}
                rounded={'md'}
                overflow={'hidden'}>
                <Image
                    h={'120px'}
                    w={'full'}
                    src={
                        'https://images.unsplash.com/photo-1612865547334-09cb8cb455da?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=634&q=80'
                    }
                    objectFit="cover"
                    alt="#"
                />
                <Flex justify={'center'} mt={-12}>
                    <Avatar
                        size={'xl'}
                        src={
                            `https://randomuser.me/api/portraits/${randomUserGender}/${imageNumber}.jpg`
                        }
                        css={{
                            border: '2px solid white',
                        }}
                    />
                </Flex>

                <Box p={6}>
                    <Stack spacing={0} align={'center'} mb={5}>
                        <Tag borderRadius={'full'}>{id}</Tag>
                        <Heading fontSize={'2xl'} fontWeight={500} fontFamily={'body'}>
                            {name}
                        </Heading>
                        <Text color={'gray.500'}>{email}</Text>
                        <Text color={'gray.500'}> Age: {age} | {gender}</Text>


                    </Stack>

                </Box>
                <Stack direction={'row'} spacing={6} justify={'center'} p={2}>
                    <Stack>
                        <UpdateCustomerDrawer
                            initialValues={{name, email, age}}
                            customerId={id}
                            fetchCustomers={fetchCustomers}
                        />

                    </Stack>

                    <Stack>
                        <DeleteCustomer
                            fetchCustomers={fetchCustomers}
                            customerId={id}
                            name={name}
                        />
                    </Stack>

                </Stack>
            </Box>
        </Center>
    )
}