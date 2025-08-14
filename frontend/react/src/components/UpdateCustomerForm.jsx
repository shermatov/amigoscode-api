import React from 'react';
import ReactDOM from 'react-dom';
import { Formik, Form, useField } from 'formik';
import * as Yup from 'yup';
import {Alert, AlertIcon, Box, Button, FormLabel, Input, Select, Stack} from "@chakra-ui/react";
import {saveCustomer, updateCustomer} from "../services/client.js";
import {errorNotification, successNotification} from "../services/notification.js";

const MyTextInput = ({ label, ...props }) => {

    const [field, meta] = useField(props);
    return (
        <Box>
            <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
            <Input className="text-input" {...field} {...props} />
            {meta.touched && meta.error ? (
                <Alert className="error" status={"error"} mt={2}>
                    <AlertIcon/>
                    {meta.error}
                </Alert>
            ) : null}
        </Box>
    );
};




// And now we can use these
const UpdateCustomerForm = ({fetchCustomers, initialValues, customerId}) => {
    return (
        <>
            <Formik
                initialValues={initialValues}

                validationSchema={Yup.object({
                    name: Yup.string()
                        .max(15, 'Must be 15 characters or less')
                        .required('Required'),

                    email: Yup.string()
                        .email('Invalid email address')
                        .required('Required'),
                    age: Yup.number()
                        .min(16, 'Must be at least 16 years of age')
                        .max(100, 'Max be at least 100 years of age')
                        .required('Required')
                })}
                onSubmit={(updatedCustomer, { setSubmitting }) => {
                    setSubmitting(true);
                    updateCustomer(customerId, updatedCustomer)
                        .then(result => {
                            console.log(result);
                            successNotification("Customer updated", `${name} was successfully updated`, result);
                            fetchCustomers();
                        }).catch(error => {
                            errorNotification(
                                error.code,
                                error.response.data.message,
                                );
                        })
                        .finally(() => {
                            setSubmitting(false);
                        });
                }}
            >
                {({isValid,isSubmitting, dirty} ) => (
                        <Form>
                            <Stack spacing={"24px"}>

                                <MyTextInput
                                    label="Name"
                                    name="name"
                                    type="text"
                                    placeholder="Jane"
                                />

                                <MyTextInput
                                    label="Email Address"
                                    name="email"
                                    type="email"
                                    placeholder="jane@formik.com"
                                />

                                <MyTextInput
                                    label="Age"
                                    name="age"
                                    type="number"
                                    placeholder="24"
                                />

                                <Button disabled={!(isValid  && dirty)|| isSubmitting} type="submit">Submit</Button>
                            </Stack>
                        </Form>
                    )
                }
            </Formik>
        </>
    );
};

export default UpdateCustomerForm;